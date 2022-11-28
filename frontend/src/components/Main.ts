import { data } from '../dummyData';
import { jDepsReader } from '../input/JDepsReader';
import { Filter, Graph } from '../transform/Graph';
import { nomnomlWriter } from '../output/NomnomlWriter';
import {LitElement, html, TemplateResult, css} from 'lit';
import {customElement, query} from 'lit/decorators.js';
import * as nomnoml from 'nomnoml';
import { NodeDetails, NodeEvent } from './NodeDetails';
import { FilterEvent, ImageSettings } from './ImageSettings';
import { ImageViewer } from './ImageViewer';

@customElement('app-main')
export class Main extends LitElement { 
	private readonly _input: Graph;
	
	@query('image-viewer')
	private _imageViewer?: ImageViewer;
	@query('image-settings')
	private _imageSettings?: ImageSettings;
	@query('node-details')
	private _nodeDetails?: NodeDetails;
	
	public constructor() {
		super();
		this._input = jDepsReader(data);
	}

	static styles = css`
		div {
			color: #444; 
		}
		.container {
			height: 100%;
			display: grid;
			grid-gap: 5px;
			grid-template-columns: 300px;
			grid-template-rows: 500px;
		}
		.sidebar {
			padding-left: 10px;
			padding-right: 10px;
			border-radius: 5px; 
		}
		.settings {
			background-color: #eee;
			grid-column: 1;
			grid-row: 1;
		}
		.details {
			background-color: #eee;
			grid-column: 1 ;
			grid-row: 2 ;
		}
		.image {
			grid-column: 2 ;
			grid-row: 1 / 3 ;
			overflow: hidden;
		}
	`;

	protected render(): TemplateResult<1> {
		return html `
			<div class="container">
				<div class="sidebar settings">
					<image-settings @filterChanged="${this.filterChanged}"></image-settings>
				</div>
				<div class="sidebar details">
					<node-details @base="${this.setBaseNode}" @ignore="${this.ignoreNode}"></node-details>
				</div>
				<div class="image">
					<image-viewer @selectNode="${this.selectNode}"></image-viewer>
				</div>
			</div>
		`;
	}

	protected updated(): void {
		this._imageViewer!.input = this._input;
		this.setFilters([]);
	}

	private setBaseNode(e: NodeEvent): boolean{
		this._imageSettings?.setBaseNode(e.node);
		return false;
	}

	private ignoreNode(e: NodeEvent): boolean{
		this._imageSettings?.ignoreNode(e.node);
		return false;
	}

	private selectNode(e: NodeEvent): boolean {
		this._nodeDetails!.node = e.node;
		return false;
	}

	private filterChanged(e: FilterEvent): boolean {
		this.setFilters(e.filters);
		return false;
	}

	private setFilters(filters: Filter[]){
		console.log('setting filters');
		//TODO Set spinner...
		setTimeout(() => {
			const graph = this._input.filter(filters);
			const output = nomnomlWriter(graph);
			const image = nomnoml.renderSvg(output);		
			this._imageViewer!.image = image;
			this._imageSettings!.image = image;
		});
	}
}
