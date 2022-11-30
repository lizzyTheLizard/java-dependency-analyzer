import { html, LitElement, TemplateResult } from 'lit';
import {customElement, property, query} from 'lit/decorators.js';
import '@polymer/paper-input/paper-textarea';
import { PaperTextareaElement } from '@polymer/paper-input/paper-textarea';

@customElement('list-selection')
export class ListSelection extends LitElement {
	@property()
	public label = '';
	@property()
	private _values: string[] = [];
	@query('paper-textarea')
	private _input?: PaperTextareaElement;

	public constructor() {
		super();
	}

	protected render(): TemplateResult<1> {
		const value=this._values.join(',');
		return html `
			<paper-textarea value="${value}" label="${this.label}" @blur="${this.changed}" placeholder="com.package"></paper-textarea>
		`;
	}

	private changed(){
		const values = this._input!.value?.split(',');
		this._values = [...new Set(values)];
		this.dispatchEvent(new CustomEvent('changed'));
	}

	public addValue(value: string){
		if(this._values.includes(value)){
			return;
		}
		this._values = [value, ...this._values];
	}

	public getValues(): string[]{
		return this._values;
	}
}
