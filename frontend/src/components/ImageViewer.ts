import {css, html, LitElement, TemplateResult} from 'lit';
import {customElement, property, query, queryAll} from 'lit/decorators.js';
import {unsafeHTML} from 'lit/directives/unsafe-html.js';
import svgPanZoom from 'svg-pan-zoom';
import {Graph} from '../transform/Graph';
import {NodeEvent} from './NodeDetails';

@customElement('image-viewer')
export class ImageViewer extends LitElement {
    static styles = css`
      svg {
        height: 100%;
        width: 100%;
        display: block;
      }
    `;
    @property()
    public image = '';
    @property()
    public input?: Graph;
    private readonly panSettings = {minZoom: 0.1, maxZoom: 100, controlIconsEnabled: true};
    @query('svg')
    private svg?: HTMLElement;
    @queryAll('g')
    private g?: HTMLElement[];

    protected render(): TemplateResult<1> {
        return html`${unsafeHTML(this.image)}`;
    }

    protected updated(): void {
        if (this.svg) {
            svgPanZoom(this.svg, this.panSettings);
        }
        this.g?.forEach(g => g.onclick = (ev: MouseEvent) => this.selectElement(ev.target));
    }

    private selectElement(target: EventTarget | null) {
        const name = this.getFullName(target as HTMLElement);
        const node = name ? this.input?.findNode(name) : undefined;
        if (node) {
            this.dispatchEvent(new NodeEvent('selectNode', node));
            return false;
        }
        return true;
    }

    private getFullName(e: HTMLElement, lastName?: string): string {
        if (e === this.svg) {
            return '';
        }
        const name = e.getAttribute('data-name');
        if (!name || name === lastName) {
            return this.getFullName(e.parentElement!, lastName);
        }
        const parentName = this.getFullName(e.parentElement!, name);
        if (!parentName) {
            return name;
        } else {
            return parentName + '.' + name;
        }
    }
}
