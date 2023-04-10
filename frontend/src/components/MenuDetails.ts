import {customElement, property} from 'lit/decorators.js';
import {css, html, LitElement, TemplateResult} from 'lit';
import {Image} from '../image/Image';
import {GraphNode} from '../transform/Graph';
import {ImageChangedEvent} from './Main';
import '@carbon/web-components/es/components/button/index.js';
import '@carbon/web-components/es/components/data-table/index.js';
import '@carbon/web-components/es/components/checkbox/index.js';
import {Attribute} from '../image/InputFile';

@customElement('menu-details')
export class MenuDetails extends LitElement {
    static styles = css`
      .grid-container {
        display: grid;
        height: 100%;
        grid-template-rows: 75px 75px auto 60px 60px 60px 60px ;
      }
      .fullName {
        width: 100%;
        overflow: hidden;
      }
      bx-btn {
        width: 100%;
        max-width: none;
        margin-bottom: 10px;
      }
    `;
    @property()
    public image?: Image;
    @property()
    public selectedNode?: GraphNode;

    protected render(): TemplateResult<1> {
        if (!this.selectedNode) {
            return html`
                <div class="grid-container">
                    <h3>No Node Selected</h3>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                </div>
            `;
        }
        if (this.selectedNode?.type === 'PACKAGE') {
            return html`
                <div class="grid-container">
                    <h3>Package Details</h3>
                    <div class="fullName">${this.selectedNode?.fullName.replaceAll('.', '.​')}</div>
                    <div></div>
                    <bx-btn @click="${this.base}">${this.isBase() ? 'Unset' : 'Set'} as Base </bx-btn>
                    <bx-btn @click="${this.ignore}">${this.isIgnored() ? 'Un-Ignore' : 'Ignore'}</bx-btn>
                    <bx-btn @click="${this.collapse}">${this.isCollapsed() ? 'Expand' : 'Collapse'}</bx-btn>
                    <div></div>
                </div>
            `;
        }
        return html`
                <div class="grid-container">
                    <h3>Class Details</h3>
                    <div class="fullName">${this.selectedNode?.fullName.replaceAll('.', '.​')}</div>
                    <bx-data-table>
                        <bx-table>
                            <bx-table-head>
                                <bx-table-header-row>
                                    <bx-table-header-cell>Name</bx-table-header-cell>
                                    <bx-table-header-cell>Value</bx-table-header-cell>
                                </bx-table-header-row>
                            </bx-table-head>
                            <bx-table-body>
                                ${this.selectedNode?.attributes?.map(a => this.renderAttribute(a))}
                            </bx-table-body>
                        </bx-table>
                    </bx-data-table>
                    <bx-btn disabled @click="${this.base}">${this.isBase() ? 'Unset' : 'Set'} as Base </bx-btn>
                    <bx-btn @click="${this.ignore}">${this.isIgnored() ? 'Un-Ignore' : 'Ignore'}</bx-btn>
                    <bx-btn disabled @click="${this.collapse}">${this.isCollapsed() ? 'Expand' : 'Collapse'}</bx-btn>
                    <div></div>
                </div>
        `;
    }

    protected renderAttribute(a: Attribute): TemplateResult<1> {
        return html `
            <bx-table-row>
                <bx-table-cell>${a.name}</bx-table-cell>
                <bx-table-cell>${this.renderAttributeValue(a)}</bx-table-cell>
            </bx-table-row>
        `;
    }

    protected renderAttributeValue(a: Attribute): TemplateResult<1> {
        switch (a.type) {
        case 'BOOLEAN':
            return html `<bx-checkbox label-text="${a.name}" disabled checked="${!!a.value}"></bx-checkbox>
            `;
        case 'LINK':
            return html `<a href="${a.value}">${a.name}</a>`;
        default:
            return html `${a.value}`;
        }
    }

    private base(){
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
