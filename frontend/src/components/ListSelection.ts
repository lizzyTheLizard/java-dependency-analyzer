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
        return html `<paper-item @click="${this.select(item)}">${this.shortenFullName(item)}</paper-item>`;
    }

    private select(item: GraphNode): (e: Event) => void {
        return () =>             this.dispatchEvent(new NodeSelectedEvent(item));
    }

    private shortenFullName(item:GraphNode): string {
        return this.shorten(item.fullName, 0);
    }

    private shorten(s: string, used = 0): string {
        if (used + s.length < 35) {
            return s;
        }
        const splitted = s.split('.');
        const first = splitted.shift();
        const remainer = splitted.join('.');
        if(!first) {
            return '';
        }
        return first[0] + '.' + this.shorten(remainer, used + 2);
    }
}
