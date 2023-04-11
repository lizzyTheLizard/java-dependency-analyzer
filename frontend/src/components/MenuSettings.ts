import {customElement, property} from 'lit/decorators.js';
import {css, html, LitElement, TemplateResult} from 'lit';
import {ImageChangedEvent, NodeSelectedEvent} from './Main';
import {BXComboBox, BXInput} from '@carbon/web-components';
import {Image} from '../logic/Image';
import {Properties, ShowClasses} from '../logic/Input';
import '@carbon/web-components/es/components/input/index.js';
import '@carbon/web-components/es/components/button/index.js';
import '@carbon/web-components/es/components/structured-list/index.js';
import '@carbon/web-components/es/components/combo-box/index.js';

//TODO: Support splits
@customElement('menu-settings')
export class MenuSettings extends LitElement {
    static styles = css`
      .grid-container {
        display: grid;
        height: 100%;
        grid-template-rows: 75px 100px 100px auto auto;
      }

      bx-structured-list-cell, bx-structured-list-header-cell {
        padding: 5px 5px 5px 5px;
      }

      bx-structured-list {
        width: 100%;
        overflow: hidden;
      }
    `;
    @property()
    private image?: Image;

    private get properties(): Properties {
        return this.image!.properties;
    }

    protected render(): TemplateResult<1> {
        return html`
            <div class="grid-container">
                <h3>Image Settings</h3>
                <bx-input @keyup="${this.baseChanged}" value="${this.properties.basePackage}">
                    <span slot="label-text">Base Package</span>
                </bx-input>
                <bx-combo-box value="${this.properties.showClasses}" @bx-combo-box-selected="${this.showClassesChanged}"
                              label-text="Show Classes">
                    <bx-combo-box-item value="HIDE_INNER">Hide Inner Classes</bx-combo-box-item>
                    <bx-combo-box-item value="HIDE_ALL">Hide All Classes</bx-combo-box-item>
                    <bx-combo-box-item value="SHOW_ALL">Show All Classes</bx-combo-box-item>
                </bx-combo-box>
                <bx-structured-list>
                    <bx-structured-list-head>
                        <bx-structured-list-header-row>
                            <bx-structured-list-header-cell></bx-structured-list-header-cell>
                            <bx-structured-list-header-cell>Collapsed</bx-structured-list-header-cell>
                        </bx-structured-list-header-row>
                    </bx-structured-list-head>
                    <bx-structured-list-body>
                        ${this.renderListRows(this.properties.collapsePackages, n => this.toggleCollapse(n))}
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
                        ${this.renderListRows(this.properties.ignoredPackages, n => this.toggleIgnored(n))}
                    </bx-structured-list-body>
                </bx-structured-list>
                <div></div>
            </div>
        `;
    }

    private renderListRows(nodes: string[], del: (n: string) => (() => void)): TemplateResult<1> {
        if (!nodes) {
            return html``;
        }
        return html`${nodes.map(n => this.renderListRow(n, del))}`;
    }

    private renderListRow(fullName: string, del: (n: string) => (() => void)): TemplateResult<1> {
        return html`
            <bx-structured-list-row>
                <bx-structured-list-cell @click="${del(fullName)}">
                    <svg focusable="false" preserveAspectRatio="xMidYMid meet" xmlns="http://www.w3.org/2000/svg"
                         fill="currentColor" aria-hidden="true" width="16" height="16" viewBox="0 0 32 32"
                         style="fill: red">
                        <path d="M16,2C8.2,2,2,8.2,2,16s6.2,14,14,14s14-6.2,14-14S23.8,2,16,2z M16,28C9.4,28,4,22.6,4,16S9.4,4,16,4s12,5.4,12,12S22.6,28,16,28z"></path>
                        <path d="M21.4 23L16 17.6 10.6 23 9 21.4 14.4 16 9 10.6 10.6 9 16 14.4 21.4 9 23 10.6 17.6 16 23 21.4z"></path>
                    </svg>
                </bx-structured-list-cell>
                <bx-structured-list-cell @click="${this.selectNode(fullName)}">
                    <div>${fullName}</div>
                </bx-structured-list-cell>
            </bx-structured-list-row>
        `;
    }

    private baseChanged(e: Event) {
        if (!this.image) {
            return;
        }
        const input = e.target as BXInput;
        const fullName = input.value;
        const newImage = this.image.update({basePackage: fullName});
        this.dispatchEvent(new ImageChangedEvent(newImage));
    }

    private showClassesChanged(e: Event) {
        if (!this.image) {
            return;
        }
        const target = e.target as BXComboBox;
        const newValue = target.value as ShowClasses;
        const newImage = this.image.update({showClasses: newValue});
        this.dispatchEvent(new ImageChangedEvent(newImage));
    }

    private toggleCollapse(fullName: string): () => boolean {
        return () => {
            if (!this.image) {
                return true;
            }
            const newImage = this.image?.toggleCollapsed(fullName);
            this.dispatchEvent(new ImageChangedEvent(newImage));
            return false;
        };
    }

    private toggleIgnored(fullName: string): () => void {
        return () => {
            if (!this.image) {
                return true;
            }
            const newImage = this.image?.toggleIgnored(fullName);
            this.dispatchEvent(new ImageChangedEvent(newImage));
            return false;
        };
    }

    private selectNode(fullName: string): () => void {
        const node = this.image?.findNode(fullName);
        return () => this.dispatchEvent(new NodeSelectedEvent(node));
    }
}
