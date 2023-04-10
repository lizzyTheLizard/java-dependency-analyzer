import {customElement, property, query} from  'lit/decorators.js';
import {css, html, LitElement, TemplateResult} from 'lit';
import {Image} from '../image/Image';
import '@carbon/web-components/es/components/input/index.js';
import '@carbon/web-components/es/components/button/index.js';
import '@carbon/web-components/es/components/structured-list/index.js';
import {GraphNode} from '../transform/Graph';
import {ImageChangedEvent, NodeSelectedEvent} from './Main';
import {BXInput} from '@carbon/web-components';

@customElement('menu-settings')
export class MenuSettings extends LitElement {
    static styles = css`
      .grid-container {
        display: grid;
        height: 100%;
        grid-template-rows: 75px 100px auto auto ;
      }
      bx-structured-list-cell, bx-structured-list-header-cell{
        padding: 5px 5px 5px 5px;
      }
      bx-structured-list {
        width: 100%;
        overflow: hidden;
      }      
    `;
    @property()
    public image?: Image;
    @query('bx-input')
    private baseInput?: BXInput;

    protected render(): TemplateResult<1> {
        return html`
                <div class="grid-container">
                    <h3>Image Settings</h3>
                    <bx-input @keyup="${this.baseChanged}" value="${this.image?.base?.fullName}">
                        <span slot="label-text">Base Package</span>
                    </bx-input>
                    <bx-structured-list>
                        <bx-structured-list-head>
                            <bx-structured-list-header-row>
                                <bx-structured-list-header-cell></bx-structured-list-header-cell>
                                <bx-structured-list-header-cell>Collapsed</bx-structured-list-header-cell>
                            </bx-structured-list-header-row>
                        </bx-structured-list-head>
                        <bx-structured-list-body>
                            ${this.renderListRows(this.image?.collapsed ?? [], n => (() => this.unCollapse(n)))}
                        </bx-structured-list-body>
                    </bx-structured-list>
                    <bx-structured-list>
                        <bx-structured-list-head>
                            <bx-structured-list-header-row>
                                <bx-structured-list-header-cell></bx-structured-list-header-cell>
                                <bx-structured-list-header-cell>Ignored</bx-structured-list-header-cell>
                            </bx-structured-list-header-row>
                        </bx-structured-list-head>
                        <bx-structured-list-body>
                            ${this.renderListRows(this.image?.ignored ?? [], n => (() => this.unIgnore(n)))}
                        </bx-structured-list-body>
                    </bx-structured-list>
                    <div></div>
                </div>
        `;
    }

    private renderListRows(nodes: GraphNode[], del: (n:GraphNode) => (() => void)): TemplateResult<1> {
        if (!nodes) {
            return html``;
        }
        return html`${nodes.map(n => this.renderListRow(n, del))}`;
    }

    private renderListRow(node: GraphNode, del: (n:GraphNode) => (() => void)): TemplateResult<1> {
        return html`
            <bx-structured-list-row>
                <bx-structured-list-cell @click="${del(node)}">
                    <svg focusable="false" preserveAspectRatio="xMidYMid meet" xmlns="http://www.w3.org/2000/svg" fill="currentColor" aria-hidden="true" width="16" height="16" viewBox="0 0 32 32" style="fill: red">
                        <path d="M16,2C8.2,2,2,8.2,2,16s6.2,14,14,14s14-6.2,14-14S23.8,2,16,2z M16,28C9.4,28,4,22.6,4,16S9.4,4,16,4s12,5.4,12,12S22.6,28,16,28z"></path>
                        <path d="M21.4 23L16 17.6 10.6 23 9 21.4 14.4 16 9 10.6 10.6 9 16 14.4 21.4 9 23 10.6 17.6 16 23 21.4z"></path>
                    </svg>
                </bx-structured-list-cell>
                <bx-structured-list-cell @click="${this.selectNode(node)}">
                    <div>${node.fullName}</div>
                </bx-structured-list-cell>
            </bx-structured-list-row>
        `;
    }

    private selectNode(node: GraphNode): () => void {
        return () => this.dispatchEvent(new NodeSelectedEvent(node));
    }

    private baseChanged(){
        const value = this.baseInput?.value;
        if(!this.image) {
            return;
        }
        if(!value){
            return;
        }
        const node = this.image.findNode(value);
        if(this.image?.base === node) {
            return;
        }
        const newImage = this.image.setBase(node);
        this.dispatchEvent(new ImageChangedEvent(newImage));
    }

    private unIgnore(node: GraphNode){
        if(!this.image) {
            return;
        }
        const newImage = this.image.ignore(node);
        this.dispatchEvent(new ImageChangedEvent(newImage));
    }

    private unCollapse(node: GraphNode){
        if(!this.image) {
            return;
        }
        const newImage = this.image.collapse(node);
        this.dispatchEvent(new ImageChangedEvent(newImage));
    }
}
