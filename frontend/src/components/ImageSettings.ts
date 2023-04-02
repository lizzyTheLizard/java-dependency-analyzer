import FileSaver from 'file-saver';
import {Image} from '../transform/Image';
import {customElement, property} from 'lit/decorators.js';
import {css, html, LitElement, TemplateResult} from 'lit';
import {ImageChangedEvent} from './Main';
import {GraphNode} from '../transform/Graph';
import '@polymer/paper-dropdown-menu/paper-dropdown-menu';
import '@polymer/paper-listbox/paper-listbox';
import '@polymer/paper-item/paper-item';
import '@polymer/paper-button/paper-button';

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
    private image?: Image;
    @property()
    private selectedNode?: GraphNode;

    protected render(): TemplateResult<1> {
        return html`
            <h3>Image Settings</h3>
            <paper-dropdown-menu label="Show Classes" @iron-select="${this.showClassesChanged}">
                <paper-listbox selected="${this.getShowClassIndex()}" slot="dropdown-content" class="dropdown-content">
                    <paper-item value="HIDE_INNER">Hide Inner Classes</paper-item>
                    <paper-item value="HIDE_ALL">Hide All Classes</paper-item>
                    <paper-item value="SHOW_ALL">Show All Classes</paper-item>
                </paper-listbox>
            </paper-dropdown-menu>
            <list-selection label="Ignored" .values="${this.image?.ignored}" .selectedNode="${this.selectedNode}"></list-selection>
            <list-selection label="Collapsed" .values="${this.image?.collapsed}" .selectedNode="${this.selectedNode}"></list-selection>
            <div class="parent">
                <paper-button class="custom pink" raised @click="${this.saveSVG}">Export SVG</paper-button>
                <paper-button class="custom pink" raised @click="${this.saveJPEG}">Export JPEG</paper-button>
            </div>
        `;
    }

    private showClassesChanged(e: CustomEvent): boolean {
        if(!this.image) {
            return true;
        }
        const newValue = e.detail.item.getAttribute( 'value');
        if(this.image?.showClasses === newValue) {
            return false;
        }
        const image = this.image.setShowClasses(newValue);
        this.dispatchEvent(new ImageChangedEvent(image));
        return false;
    }

    private getShowClassIndex(): number | undefined {
        const showClasses = this.image?.showClasses;
        switch (showClasses) {
        case 'HIDE_INNER':
            return 0;
        case 'HIDE_ALL':
            return 1;
        case 'SHOW_ALL':
            return 2;
        default:
            return undefined;
        }
    }

    private saveSVG() {
        if(!this.image) {
            return;
        }
        //TODO Set Spinner
        const fileContend = this.image.getSvgImage();
        const file = new File([fileContend], 'image.svg', {type: 'image/svg+xml'});
        FileSaver.saveAs(file);
    }

    private saveJPEG() {
        alert('not implemented yet');
    }
}
