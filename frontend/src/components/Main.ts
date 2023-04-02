import {GraphNode} from '../transform/Graph';
import {css, html, LitElement, TemplateResult} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import {Image} from '../transform/Image';

export class ImageChangedEvent extends Event{
    constructor(public readonly image: Image) {
        super('imageChanged');
    }
}

export class NodeSelectedEvent extends Event {
    constructor(public readonly node?: GraphNode) {
        super('nodeSelected', {bubbles: true, composed: true});
    }
}

@customElement('app-main')
export class Main extends LitElement {
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
        grid-column: 1;
        grid-row: 2;
      }

      .image {
        grid-column: 2;
        grid-row: 1 / 3;
        overflow: hidden;
      }
    `;
    @property()
    private _selectedNode?: GraphNode;
    @property()
    private _image: Image;

    public constructor() {
        super();
        this._image = new Image();
    }

    protected render(): TemplateResult<1> {
        return html`
            <div class="container">
                <div class="sidebar settings">
                    <image-settings .image="${this._image}" .selectedNode=${this._selectedNode}
                                    @imageChanged="${this.imageChanged}" @nodeSelected="${this.nodeSelected}">
                    </image-settings>
                </div>
                <div class="sidebar details">
                    <node-details .image="${this._image}" .selectedNode=${this._selectedNode}
                                  @imageChanged="${this.imageChanged}">
                    </node-details>
                </div>
                <div class="image">
                    <image-viewer .image="${this._image}"
                                  @nodeSelected="${this.nodeSelected}"></image-viewer>
                </div>
            </div>
        `;
    }

    private imageChanged(e: ImageChangedEvent): boolean {
        this._image = e.image;
        return false;
    }

    private nodeSelected(e: NodeSelectedEvent): boolean {
        this._selectedNode = e.node;
        return false;
    }
}
