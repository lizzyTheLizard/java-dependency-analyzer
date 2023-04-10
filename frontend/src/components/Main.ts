import {GraphNode} from '../transform/Graph';
import {css, html, LitElement, TemplateResult} from 'lit';
import {property, customElement} from  'lit/decorators.js';
import {Image} from '../image/Image';
import '@carbon/web-components/es/components/ui-shell/index.js';

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
      .sidebar{
        position: absolute;
        left: 10px;
        right: 100px;
        top: 40px;
        bottom: 0;
        height: 100%;
        overflow: hidden;
        z-index: 1000;
        background-color: white;
      }
      
      image-viewer {
        position: absolute;
        top:30px;
        bottom: 0;
        right:0;
        left: 100px;
      }

      .divider {
        position: absolute;
        top: 30px;
        bottom: 0;
        width: 5px;
        background-color: grey;
        cursor: ew-resize;
        z-index: 2000;
      }
    `;
    @property()
    private _selectedNode?: GraphNode;
    @property()
    private _image: Image;
    @property()
    private _showSidenav = true;
    @property()
    private _sidenavWidth = 300;
    @property()
    private _menuToShow = 'FILE';

    private _draggingDivider = false;
    private _startDraggingWidth = 0;
    private _startMouseX = 0;

    public constructor() {
        super();
        this._image = new Image();
    }

    protected render(): TemplateResult<1> {
        return html`
            <div 
                    @mousemove="${this.mouseMoved}"
                    @mouseup="${this.stopDraggingDivider}"
                    @mouseleave="${this.stopDraggingDivider}"
            >
                <bx-header style="height:30px">
                    <bx-header-name @click="${this.toggleMenu()}">Dependency Graph</bx-header-name>
                    <bx-header-nav>
                        <bx-header-nav-item @click="${this.toggleMenu('FILE')}">File</bx-header-nav-item>
                        <bx-header-nav-item @click="${this.toggleMenu('SETTINGS')}">Settings</bx-header-nav-item>
                        <bx-header-nav-item @click="${this.toggleMenu('DETAILS')}">Node Details</bx-header-nav-item>
                    </bx-header-nav>
                </bx-header>
                <div class="sidebar" style="display:${this._showSidenav?'inherit':'none'};width:${this._sidenavWidth-10}px;">
                    <menu-file style="display:${this._menuToShow == 'FILE' ? 'inherit' : 'none'}; width:${this._sidenavWidth-20}px; height: 100%;" 
                               .image="${this._image}" 
                               .selectedNode="${this._selectedNode}" 
                               @nodeSelected="${this.nodeSelected}" 
                               @imageChanged="${this.imageChanged}"
                    ></menu-file>
                    <menu-settings style="display:${this._menuToShow == 'SETTINGS' ? 'inherit' : 'none'}; width:${this._sidenavWidth-20}px;height: 100%;" 
                                   .image="${this._image}"
                                   .selectedNode="${this._selectedNode}" 
                                   @nodeSelected="${this.nodeSelected}" 
                                   @imageChanged="${this.imageChanged}"
                    ></menu-settings>
                    <menu-details style="display:${this._menuToShow == 'DETAILS' ? 'inherit' : 'none'}; width:${this._sidenavWidth-20}px;height: 100%;" 
                                  .image="${this._image}"
                                  .selectedNode="${this._selectedNode}" 
                                  @nodeSelected="${this.nodeSelected}" 
                                  @imageChanged="${this.imageChanged}"
                    ></menu-details>
                </div>
                <image-viewer .image="${this._image}" @nodeSelected="${this.nodeSelected}"></image-viewer>
                <div class="divider" style="display:${this._showSidenav?'inherit':'none'};left:${this._sidenavWidth}px;"
                     @mousedown="${this.startDraggingDivider}"
                ></div>
            </div>
        `;
    }

    private startDraggingDivider(e: MouseEvent) {
        this._draggingDivider = true;
        this._startDraggingWidth = this._sidenavWidth;
        this._startMouseX = e.x;
    }

    private stopDraggingDivider() {
        this._draggingDivider = false;
    }

    mouseMoved(e: MouseEvent) {
        if(!this._draggingDivider) {
            return;
        }
        const moved = e.x - this._startMouseX;
        this._sidenavWidth = Math.max(100, this._startDraggingWidth + moved);
    }

    private toggleMenu(type?: string): () => void {
        if(!type || this._menuToShow === type) {
            return () => this._showSidenav = !this._showSidenav;
        }
        return () => {
            this._menuToShow = type;
            this._showSidenav = true;
        };
    }

    private imageChanged(e: ImageChangedEvent): boolean {
        this._image = e.image;
        return false;
    }

    private nodeSelected(e: NodeSelectedEvent): boolean {
        this._selectedNode = e.node;
        if(this._selectedNode) {
            this._showSidenav = true;
            this._menuToShow = 'DETAILS';
        }
        return false;
    }
}
