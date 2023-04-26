import {customElement, property} from 'lit/decorators.js';
import {css, html, LitElement, TemplateResult} from 'lit';
import {ImageChangedEvent} from './Main';
import {Image} from '../logic/Image';
import '@carbon/web-components/es/components/button/index.js';
import '@carbon/web-components/es/components/data-table/index.js';
import '@carbon/web-components/es/components/checkbox/index.js';
import {Attribute} from '../logic/Input';
import {ImageNode} from '../logic/ImageNode';

@customElement('menu-details')
export class MenuDetails extends LitElement {
    static styles = css`
      .grid-container {
        display: grid;
        height: 100%;
        grid-template-rows: 75px 75px auto 60px 60px 60px 60px 60px;
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
    public selectedNode?: ImageNode;
    @property()
    private image?: Image;

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
                    <bx-btn @click="${this.setBase}">${this.isBase() ? 'Unset' : 'Set'} as Base</bx-btn>
                    <bx-btn @click="${this.toggleIgnored}">${this.isIgnored() ? 'Un-Ignore' : 'Ignore'}</bx-btn>
                    <bx-btn @click="${this.toggleCollapsed}">${this.isCollapsed() ? 'Expand' : 'Collapse'}</bx-btn>
                    <bx-btn @click="${this.toggleSplitted}">${this.isSplitted() ? 'Un-Split' : 'Split'}</bx-btn>
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
                <bx-btn disabled @click="${this.setBase}">${this.isBase() ? 'Unset' : 'Set'} as Base</bx-btn>
                <bx-btn @click="${this.toggleIgnored}">${this.isIgnored() ? 'Un-Ignore' : 'Ignore'}</bx-btn>
                <bx-btn disabled @click="${this.toggleCollapsed}">${this.isCollapsed() ? 'Expand' : 'Collapse'}</bx-btn>
                <bx-btn disabled @click="${this.toggleSplitted}">${this.isSplitted() ? 'Un-Split' : 'Split'}</bx-btn>
                <div></div>
            </div>
        `;
    }

    private renderAttribute(a: Attribute): TemplateResult<1> {
        return html`
            <bx-table-row>
                <bx-table-cell>${a.name}</bx-table-cell>
                <bx-table-cell>${this.renderAttributeValue(a)}</bx-table-cell>
            </bx-table-row>
        `;
    }

    private renderAttributeValue(a: Attribute): TemplateResult<1> {
        switch (a.type) {
        case 'BOOLEAN':
            return html`
                <bx-checkbox label-text="${a.name}" disabled checked="${!!a.value}"></bx-checkbox>
            `;
        case 'LINK':
            return html`<a href="${a.value}">${a.name}</a>`;
        default:
            return html`${a.value}`;
        }
    }

    private setBase() {
        if (!this.selectedNode || !this.image) {
            return false;
        }
        const newImage = this.image.update({basePackage: this.selectedNode.fullName});
        this.dispatchEvent(new ImageChangedEvent(newImage));
        return false;
    }

    private toggleIgnored(): boolean {
        if (!this.selectedNode || !this.image) {
            return false;
        }
        const newImage = this.image?.toggleIgnored(this.selectedNode.fullName);
        this.dispatchEvent(new ImageChangedEvent(newImage));
        return false;
    }

    private toggleCollapsed(): boolean {
        if (!this.selectedNode || !this.image) {
            return false;
        }
        const newImage = this.image?.toggleCollapsed(this.selectedNode.fullName);
        this.dispatchEvent(new ImageChangedEvent(newImage));
        return false;
    }

    private toggleSplitted(): boolean {
        if (!this.selectedNode || !this.image) {
            return false;
        }
        const newImage = this.image?.toggleSplit(this.selectedNode.fullName);
        this.dispatchEvent(new ImageChangedEvent(newImage));
        return false;
    }

    private isBase(): boolean {
        return this.image?.properties.basePackage === this.selectedNode?.fullName;
    }

    private isIgnored(): boolean {
        return this.image?.properties.ignoredPackages
            .includes(this.selectedNode?.fullName ?? '') ?? false;
    }

    private isCollapsed(): boolean {
        return this.image?.properties.collapsePackages
            .includes(this.selectedNode?.fullName ?? '') ?? false;
    }

    private isSplitted(): boolean {
        return this.image?.properties.splitPackages
            .includes(this.selectedNode?.fullName ?? '') ?? false;
    }

}
