import {GraphNode} from '../transform/Graph';
import { css, html, LitElement, TemplateResult } from 'lit';
import {customElement, property} from 'lit/decorators.js';

export class NodeEvent extends Event {
	constructor(type: string, public readonly node: GraphNode) {
		super(type);
	}
}

@customElement('node-details')
export class NodeDetails extends LitElement {
	@property()
	public node?: GraphNode;

	static styles = css`
		.table-wrapper {
			width: 100%;
			overflow-y: scroll;
			overflow-x: hidden;
		}
		.twoButtons {
			width: 100%;
			display: flex;
		}
		paper-button {
			flex: 50%;
		}
		paper-button.pink {
			background-color: indigo;
			color: white;
		}
	`;

	protected render(): TemplateResult<1> {
		if(!this.node) {
			return html ` 
				<h3>Node Details</h3>
				<div>None Selected</div>
			`;
		}
		return html `
			<h3>Node Details</h3>
			<div class="table-wrapper">
				<table class="table">
					<tr>
						<td>Name</td>
						<td>${this.node?.name}</td>
					</tr>
					<tr>
						<td>Full</td>
						<td>${this.node?.fullName}</td>
					</tr>
					<tr>
						<td>Type</td>
						<td>${this.node?.type}</td>
					</tr>
				</table>
			</div>
			<div class="twoButtons">
				<paper-button class="custom pink" raised @click="${this.base}" ${(this.node?.type == 'CLASS') ? 'disabled' : ''}>Set Base</paper-button>		
				<paper-button class="custom pink" raised @click="${this.ignore}">Ignore</paper-button>		
			</div>
		`;
	}

	private base(){
		this.dispatchEvent(new NodeEvent('base', this.node!));
	}

	private ignore(){
		this.dispatchEvent(new NodeEvent('ignore', this.node!));
	}
}
