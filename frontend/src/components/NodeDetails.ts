import {GraphNode} from '../transform/Graph';
import {css, html, LitElement, TemplateResult} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import {Image} from '../transform/Image';
import {ImageChangedEvent} from './Main';


@customElement('node-details')
export class NodeDetails extends LitElement {
    static styles = css`
      .table-wrapper {
        width: 100%;
        overflow-y: scroll;
        overflow-x: hidden;
      }

      .threeButtons {
        width: 100%;
        display: flex;
      }

      paper-button {
        flex: 33%;
      }

      paper-button.pink {
        background-color: indigo;
        color: white;
      }
    `;
    @property()
    public image?: Image;
    @property()
    public selectedNode?: GraphNode;

    protected render(): TemplateResult<1> {
        if (!this.selectedNode) {
            return html`
                <h3>Node Details</h3>
                <div>None Selected</div>
            `;
        }
        return html`
            <h3>Node Details</h3>
            <div class="table-wrapper">
                <table class="table">
                    <tr>
                        <td>Name</td>
                        <td>${this.selectedNode?.name}</td>
                    </tr>
                    <tr>
                        <td>Full</td>
                        <td>${this.selectedNode?.fullName}</td>
                    </tr>
                    <tr>
                        <td>Type</td>
                        <td>${this.selectedNode?.type}</td>
                    </tr>
                </table>
            </div>
            <div class="threeButtons">
                <paper-button class="custom pink" raised @click="${this.base}" ?disabled=${!this.isClass()}>${this.isBase() ? 'Unbase': 'Base'}</paper-button>
                <paper-button class="custom pink" raised @click="${this.ignore}">${this.isIgnored() ? 'Unignore': 'Ignore'}</paper-button>
                <paper-button class="custom pink" raised @click="${this.collapse}" ?disabled=${!this.isClass()}>${this.isCollapsed() ? 'Uncollapse': 'Collapse'}</paper-button>
            </div>
        `;
    }

    private isClass(): boolean {
        if(!this.selectedNode || !this.image) {
            return false;
        }
        return this.selectedNode.type !== 'CLASS';

    }

    private base() {
        const image = this.isBase() ? this.image!.setBase() : this.image!.setBase(this.selectedNode);
        this.dispatchEvent(new ImageChangedEvent(image));
        return false;
    }

    private isBase(): boolean {
        if(!this.selectedNode || !this.image) {
            return false;
        }
        return this.image.base === this.selectedNode;
    }

    private ignore(): boolean {
        if(!this.image || !this.selectedNode) {
            return true;
        }
        const image = this.image.ignore(this.selectedNode);
        this.dispatchEvent(new ImageChangedEvent(image));
        return false;
    }

    private isIgnored(): boolean {
        if(!this.selectedNode || !this.image) {
            return false;
        }
        return this.image.ignored.includes(this.selectedNode);
    }

    private collapse() {
        if(!this.image || !this.selectedNode) {
            return true;
        }
        const image = this.image.collapse(this.selectedNode);
        this.dispatchEvent(new ImageChangedEvent(image));
        return false;
    }

    private isCollapsed(): boolean {
        if(!this.selectedNode || !this.image) {
            return false;
        }
        return this.image.collapsed.includes(this.selectedNode);
    }
}
