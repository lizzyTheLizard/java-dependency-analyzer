import {Filter, GraphNode} from '../transform/Graph';
import {customElement, property, query} from 'lit/decorators.js';
import {css, html, LitElement, TemplateResult} from 'lit';
import {removeClasses, RemoveClassesSelection} from '../transform/RemoveClasses';
import {basePackage} from '../transform/BasePackage';
import {removePackages} from '../transform/RemovePackages';
import {ListSelection} from './ListSelection';
import '@polymer/paper-input/paper-input';
import '@polymer/paper-dropdown-menu/paper-dropdown-menu';
import '@polymer/paper-item/paper-item';
import '@polymer/paper-listbox/paper-listbox';
import '@polymer/paper-button/paper-button';
import {PaperInputElement} from '@polymer/paper-input/paper-input';
import {PaperDropdownMenuElement} from '@polymer/paper-dropdown-menu/paper-dropdown-menu';
import {PaperItemElement} from '@polymer/paper-item/paper-item';
import FileSaver from 'file-saver';
import {collapsePackages} from '../transform/CollapsePackages';

// noinspection TypeScriptValidateTypes
export class FilterEvent extends Event {
    constructor(type: string, public readonly filters: Filter[]) {
        super(type);
    }
}

@customElement('image-settings')
export class ImageSettings extends LitElement {
    static styles = css`
      paper-dropdown-menu {
        width: 100%;
      }

      .parent {
        width: 100%;
        display: flex;
      }

      paper-button {
        flex: 50%;
      }

      paper-button.pink {
        background-color: indigo;
        color: white;
      }
    `;
    @property()
    public image = '';
    @query('#base')
    private _baseInput?: PaperInputElement;
    @query('#showClasses')
    private _showClasses?: PaperDropdownMenuElement;
    @query('#ignored')
    private _ignored?: ListSelection;
    @query('#collapsed')
    private _collapsed?: ListSelection;

    public setBaseNode(node: GraphNode) {
        this._baseInput!.value = node.fullName;
        this.updateFilters();
    }

    public ignoreNode(node: GraphNode) {
        this._ignored!.addValue(node.fullName);
        this.updateFilters();
    }

    public collapseNode(node: GraphNode) {
        this._collapsed!.addValue(node.fullName);
        this.updateFilters();
    }

    protected render(): TemplateResult<1> {
        return html`
            <h3>Image Settings</h3>
            <paper-input @blur="${this.baseChanged}" label="Base Package" id="base"></paper-input>
            <paper-dropdown-menu label="Show Classes" @iron-select="${this.showClassesChanged}" id="showClasses">
                <paper-listbox slot="dropdown-content" class="dropdown-content">
                    <paper-item value="HIDE_INNER">Hide Inner Classes</paper-item>
                    <paper-item value="HIDE_ALL">Hide All Classes</paper-item>
                    <paper-item value="SHOW_ALL">Show All Classes</paper-item>
                </paper-listbox>
            </paper-dropdown-menu>
            <list-selection id="ignored" label="Ignored" @changed="${this.ignoreChanged}"></list-selection>
            <list-selection id="collapsed" label="Collapsed" @changed="${this.collapsedChanged}"></list-selection>
            <div class="parent">
                <paper-button class="custom pink" raised @click="${this.saveSVG}">Export SVG</paper-button>
                <paper-button class="custom pink" raised @click="${this.saveJPEG}">Export JPEG</paper-button>
            </div>
        `;
    }

    private saveSVG() {
        const fileContend = this.image;
        const file = new File([fileContend], 'image.svg', {type: 'image/svg+xml'});
        FileSaver.saveAs(file);
    }

    private saveJPEG() {
        alert('not implemented yet');
    }

    private baseChanged(): boolean {
        this.updateFilters();
        return false;
    }

    private showClassesChanged(): boolean {
        this.updateFilters();
        return false;
    }

    private ignoreChanged(): boolean {
        this.updateFilters();
        return false;
    }

    private collapsedChanged(): boolean {
        this.updateFilters();
        return false;

    }

    private updateFilters() {
        const filters = this.generateFilters();
        this.dispatchEvent(new FilterEvent('filterChanged', filters));
    }

    private generateFilters(): Filter[] {
        const filters: Filter[] = [];

        const showClasses = (this._showClasses!.selectedItem as PaperItemElement)?.getAttribute('value') ?? 'HIDE_INNER';
        filters.push(removeClasses(showClasses as RemoveClassesSelection));

        const basePackageInput = this._baseInput!.value as string;
        if (basePackageInput) {
            filters.push(basePackage(basePackageInput));
        }

        filters.push(...removePackages(this._ignored?.getValues() ?? []));
        filters.push(...collapsePackages(this._collapsed?.getValues() ?? []));
        return filters;
    }
}
