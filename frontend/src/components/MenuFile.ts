import {customElement, property} from  'lit/decorators.js';
import {css, html, LitElement, PropertyValues, TemplateResult} from 'lit';
import {Image} from '../image/Image';
import {InputFile} from '../image/InputFile';
import {ImageChangedEvent} from './Main';
import FileSaver from 'file-saver';
import '@carbon/web-components/es/components/button/index.js';
import '@carbon/web-components/es/components/data-table/index.js';

@customElement('menu-file')
export class MenuFile extends LitElement {
    static styles = css`
      .grid-container {
        display: grid;
        height: 100%;
        grid-template-rows: 150px auto 60px 60px 60px 60px ;
      }
      bx-btn {
        width: 100%;
        margin-bottom: 10px;
        max-width: none;
      }
    `;
    @property()
    public image?: Image;
    @property()
    public file: InputFile;

    constructor() {
        super();
        this.file = new InputFile();
    }

    protected render(): TemplateResult<1> {
        return html`
                <div class="grid-container">
                    <h3>File Details</h3>
                    <bx-data-table>
                        <bx-table>
                            <bx-table-head>
                                <bx-table-header-row>
                                    <bx-table-header-cell>Name</bx-table-header-cell>
                                    <bx-table-header-cell>Value</bx-table-header-cell>
                                </bx-table-header-row>
                            </bx-table-head>
                            <bx-table-body>
                                <bx-table-row>
                                    <bx-table-cell>Model Name</bx-table-cell>
                                    <bx-table-cell>${this.file?.name}</bx-table-cell>
                                </bx-table-row>
                                <bx-table-row>
                                    <bx-table-cell>Version</bx-table-cell>
                                    <bx-table-cell>${this.file?.version}</bx-table-cell>
                                </bx-table-row>
                                <bx-table-row>
                                    <bx-table-cell>Nodes</bx-table-cell>
                                    <bx-table-cell>${this.file?.numberOfNodes}</bx-table-cell>
                                </bx-table-row>
                                <bx-table-row>
                                    <bx-table-cell>Dependencies</bx-table-cell>
                                    <bx-table-cell>${this.file?.numberOfDependencies}</bx-table-cell>
                                </bx-table-row>
                            </bx-table-body>
                        </bx-table>
                    </bx-data-table>
                    <bx-btn @click="${this.exportSvg}">Export SVG</bx-btn>
                    <bx-btn @click="${this.exportFile}">Export Text</bx-btn>
                    <bx-btn @click="${this.openFile}">Open File</bx-btn>
                    <div></div>
                </div>
        `;
    }

    protected firstUpdated(_changedProperties: PropertyValues) {
        super.firstUpdated(_changedProperties);
        this.dispatchEvent(new ImageChangedEvent(this.file.getInitialImage()));
    }

    private exportSvg(){
        if(!this.image) {
            return;
        }
        //TODO Think about adding a spinner here
        const fileContend = this.image.getSvgImage();
        const file = new File([fileContend], 'image.svg', {type: 'image/svg+xml'});
        FileSaver.saveAs(file);
    }

    private exportFile(){
        if(!this.image) {
            return;
        }
        const fileContend = this.file.toString(this.image);
        const file = new File([fileContend], 'image.json', {type: 'application/json'});
        FileSaver.saveAs(file);
    }

    private openFile() {
        const input = document.createElement('input');
        input.type = 'file';
        input.click();
        input.onchange = () => input.files ? this.fileSelected(input.files[0]) : {};
    }

    private fileSelected(file: File){
        const reader = new FileReader();
        reader.readAsText(file, 'UTF-8');
        reader.onload = (e) => {
            this.file = new InputFile(e.target?.result as string);
            this.dispatchEvent(new ImageChangedEvent(this.file.getInitialImage()));
        };
        reader.onerror = (e) => {
            alert('Could not read file:' + e.toString());
        };
    }
}
