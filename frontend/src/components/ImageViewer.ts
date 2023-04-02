import {css, html, LitElement, TemplateResult} from 'lit';
import {customElement, property, query, queryAll} from 'lit/decorators.js';
import {unsafeHTML} from 'lit/directives/unsafe-html.js';
import svgPanZoom from 'svg-pan-zoom';
import {Image} from '../transform/Image';
import {NodeSelectedEvent} from './Main';

@customElement('image-viewer')
export class ImageViewer extends LitElement {
    static styles = css`
      svg {
        height: 100%;
        width: 100%;
        display: block;
      }
    `;
    private readonly panSettings = {minZoom: 0.1, maxZoom: 100, controlIconsEnabled: true};
    @property()
    private image?: Image;
    @query('svg')
    private svg?: HTMLElement;
    @queryAll('g')
    private g?: HTMLElement[];

    protected render(): TemplateResult<1> {
        //TODO Set spinner...
        return html`${unsafeHTML(this.image?.getSvgImage() ?? '')}`;
    }

    protected updated(): void {
        if (this.svg) {
            svgPanZoom(this.svg, this.panSettings);
        }
        this.g?.forEach(g => g.onclick = (e: MouseEvent) => this.selectElement(g, e.target as HTMLElement));
    }

    private selectElement(source: HTMLElement, target: HTMLElement | null) {
        //We only want exactly one event per element
        if(!this.isUpperMost(source, target)) { return; }
        const name = this.getFullName(target as HTMLElement);
        const node = name ? this.image?.findNode(name) : undefined;
        if (node) {
            this.dispatchEvent(new NodeSelectedEvent(node));
            return false;
        }
        return true;
    }

    private isUpperMost(source: HTMLElement, target: HTMLElement | null){
        if(target?.tagName === 'g' && target === source) {
            return true;
        }
        return target?.tagName !== 'g' && target?.parentElement === source;

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
