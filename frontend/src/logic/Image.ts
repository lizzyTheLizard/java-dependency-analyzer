import {data} from '../dummyData';
import {Dependency, Input, NodeDefinition, Properties} from './Input';
import {ImageNode} from './ImageNode';
import {Graph} from './Graph';
import {writeSvg} from './WriteSvg';

export class Image {
    public readonly properties: Properties;
    private readonly nodes: NodeDefinition[];
    private readonly dependencies: Dependency[];

    public constructor(readonly input: Input) {
        this.properties = input.properties;
        this.nodes = input.nodes;
        this.dependencies = input.dependencies;
    }

    public get numberOfNodes(): number {
        return this.nodes.length;
    }

    public get numberOfDependencies(): number {
        return this.dependencies.length;
    }

    public update(newProperties: Partial<Properties>): Image {
        return new Image({...this.input, properties: {...this.input.properties, ...newProperties}});
    }

    public toggleIgnored(node: string): Image {
        const newList = this.properties.ignoredPackages.includes(node) ?
            this.properties.ignoredPackages.filter(x => x != node) :
            [...this.properties.ignoredPackages, node];
        return this.update({ignoredPackages: newList});
    }

    public toggleCollapsed(node: string): Image {
        const newList = this.properties.collapsePackages.includes(node) ?
            this.properties.collapsePackages.filter(x => x != node) :
            [...this.properties.collapsePackages, node];
        return this.update({collapsePackages: newList});
    }

    public toggleSplit(node: string): Image {
        const newList = this.properties.splitPackages.includes(node) ?
            this.properties.splitPackages.filter(x => x != node) :
            [...this.properties.splitPackages, node];
        return this.update({splitPackages: newList});
    }

    public toString(): string {
        return JSON.stringify(this.input, null, 2);
    }

    public findNode(fullName: string): ImageNode {
        const classNode = this.nodes.findLast(x => x.fullName === fullName);
        return classNode ?
            {attributes: classNode.attributes, type: 'CLASS', fullName: classNode.fullName} :
            {attributes: [], type: 'PACKAGE', fullName: fullName};
    }

    public getSvgImage(): string {
        const graph = Graph.createFrom(this.nodes)
            .removeClasses(this.properties.showClasses)
            .removeIgnored(this.properties.ignoredPackages)
            .collapseCollapsed(this.properties.collapsePackages)
            .splitSplitted(this.properties.splitPackages)
            .takeBase(this.properties.basePackage)
            .addDependencies(this.dependencies);
        return writeSvg(graph);
    }
}

export function fromDefault(): Image {
    return fromFile(data);
}

export function fromFile(inputString: string): Image {
    const input = JSON.parse(inputString);
    return new Image(input);
}
