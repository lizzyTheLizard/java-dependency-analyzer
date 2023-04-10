import {data} from '../dummyData';
import {GraphBuilder} from './GraphBuilder';
import {Image} from './Image';

export interface Input {
    properties: Properties,
    nodes: NodeDefinition[],
    dependencies: Dependency[],
}

export interface Properties {
    name: string,
    version: string,
    basePackage?: string,
    collapsePackages: string[],
    ignoredPackages: string[],
    splitPackages: string[],
}

export type Dependency = {
    readonly from: string;
    readonly to: string;
};

export interface NodeDefinition {
    fullName: string,
    attributes: Attribute[]
}

export type AttributeType = 'LINK' | 'TEXT' | 'BOOLEAN' | 'NUMBER';

export interface Attribute {
    type: AttributeType;
    value: string;
    name: string;
}

export class InputFile {
    public readonly name: string;
    public readonly version: string;
    public readonly numberOfNodes: number;
    public readonly numberOfDependencies: number;
    private readonly _input: Input;

    public constructor(inputString: string = data) {
        this._input = JSON.parse(inputString);
        this.name = this._input.properties.name;
        this.version = this._input.properties.version;
        this.numberOfDependencies = this._input.dependencies.length;
        this.numberOfNodes = this._input.nodes.length;
    }

    public getInitialImage(): Image {
        //TODO: Use "split"
        //TODO: This is really un-performant...
        const graphBuilder = new GraphBuilder(this._input);
        let image = new Image(graphBuilder);
        this._input.properties.collapsePackages.forEach(p => image = image.collapse(image.findNode(p)));
        this._input.properties.ignoredPackages.forEach(p => image = image.ignore(image.findNode(p)));
        if(this._input.properties.basePackage){
            image = image.setBase(image.findNode(this._input.properties.basePackage));
        }
        return image;
    }

    public toString(currentImage?: Image): string {
        //TODO: Overwrite properties
        console.log(currentImage);
        const input: Input = {
            ...this._input
        };
        return JSON.stringify(input, null, 2);

    }
}
