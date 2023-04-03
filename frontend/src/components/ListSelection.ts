import {css, html, LitElement, TemplateResult} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import '@polymer/paper-listbox/paper-listbox';
import {GraphNode} from '../transform/Graph';
import {NodeSelectedEvent} from './Main';

@customElement('list-selection')
export class ListSelection extends LitElement {
    static styles = css`
      paper-listbox {
        --paper-item-min-height: 10px;
        overflow-y: scroll;
        height: 100px;
      }
      paper-item {
        overflow-x: clip;
      }
    `;
    @property()
    public label = '';
    @property()
    private values: GraphNode[] = [];
    @property()
    private selectedNode?: GraphNode;

    protected render(): TemplateResult<1> {
        const items = this.values.sort((a,b) => a.fullName.localeCompare(b.fullName));
        const itemsHtml = items.map(v => this.renderItem(v));
        const selectedIndex = this.selectedNode ? items.indexOf(this.selectedNode) : undefined;
        return html`${this.label}<paper-listbox selected="${selectedIndex}">${itemsHtml}</paper-listbox>`;
    }

    private renderItem(item: GraphNode): TemplateResult<1> {
        return html `<paper-item @click="${this.select(item)}">${item.getShortenFullName(35)}</paper-item>`;
    }

    private select(item: GraphNode): (e: Event) => void {
        return () => this.dispatchEvent(new NodeSelectedEvent(item));
    }
}
