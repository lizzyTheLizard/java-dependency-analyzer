import {Dependency} from './Input';
import {Graph} from './Graph';

//TODO: Merge with Graph?
//TODO: Tests
export class DependencyGraph {
    private _dependencies: Dependency[];
    public readonly nodes: DependencyGraph[];

    constructor(public readonly graph: Graph) {
        this.nodes = graph.nodes.map(n => new DependencyGraph(n));
        this._dependencies = [];
    }

    public toNomnomlText(): string {
        return this.nodes
            .map(n => this.toNomnomlTextNode(n, ''))
            .join('');
    }

    private toNomnomlTextNode(input: DependencyGraph, intend: string): string {
        const nodeName = input.graph.getName(this.graph);
        if(input.graph.nodeDefinition) {
            //This is a class => draw it normally
            return `${intend}[${nodeName}]\n`;
        }
        const innerIntend = intend + '  ';
        const children = input.nodes
            .map(n => input.toNomnomlTextNode(n, innerIntend))
            .join('');
        const dependencies = input._dependencies
            .map(d => `${innerIntend}[${d.from}] --> [${d.to}]\n`)
            .join('');
        const content = children + dependencies;
        if(content.length === 0) {
            //This is a package without content => draw it simply
            return `${intend}[<package> ${nodeName}]\n`;
        }
        //This is a package with content => draw it simply
        return `${intend}[<package> ${nodeName}|\n${content}${intend}]\n`;
    }

    public addDependency(inputDependency: Dependency) {
        const commonParent = this.findCommonParent(inputDependency.from, inputDependency.to);
        commonParent.addDependencyToThis(inputDependency);
    }

    private addDependencyToThis(inputDependency: Dependency) {
        const fromNodeInParent = this.getChildInDirection(inputDependency.from);
        if(!fromNodeInParent) {
            return;
        }
        const toNodeInParent = this.getChildInDirection(inputDependency.to);
        if(!toNodeInParent) {
            return;
        }
        const dependency ={
            from: fromNodeInParent.getName(this),
            to: toNodeInParent.getName(this)
        };
        if(this._dependencies.find(d => d.from === dependency.from && d.to === dependency.to)) {
            return;
        }
        this._dependencies = [...this._dependencies, dependency];
    }

    private findCommonParent(fullName1: string, fullName2: string): DependencyGraph {
        const nextLevel = this.nodes.find(n => {
            const name = n.graph.fullName;
            return fullName1.startsWith(name) && fullName2.startsWith(name);
        });
        return nextLevel ? nextLevel.findCommonParent(fullName1, fullName2) : this;
    }

    private getChildInDirection(fullName: string): DependencyGraph | undefined {
        return this.nodes.find(n => fullName.startsWith(n.graph.fullName));
    }

    private getName(parent: DependencyGraph): string {
        return this.graph.getName(parent.graph);
    }
}

export function initializeDependencyGraph(graph: Graph, dependencies: Dependency[]): DependencyGraph{
    const dependencyGraph = new DependencyGraph(graph);
    dependencies.forEach(d => dependencyGraph.addDependency(d));
    return dependencyGraph;
}
