import {Dependency, NodeDefinition, ShowClasses} from './Input';

export interface DependencyGraph {
    name: string;
    nodeDefinition?: NodeDefinition;
    dependencies: Dependency[];
    nodes: DependencyGraph[];
}

export class Graph {
    private constructor(
        private nodes: Graph[],
        private readonly fullName: string,
        private readonly nodeDefinition: NodeDefinition | undefined
    ) {  }

    public static createFrom(nodes: NodeDefinition[]) {
        const graph = new Graph([],'', undefined);
        nodes.forEach(n => graph.addNode(n));
        return graph;
    }

    private addNode(node: NodeDefinition) {
        const prefixNode = this.findNodeThatIsPrefix(node.fullName);
        if (prefixNode) {
            prefixNode.addNode(node);
            return;
        }
        const siblingNode = this.findSiblingNode(node.fullName);
        if (siblingNode) {
            const newParent = this.createSharedParent(siblingNode, node);
            this.nodes = [newParent, ...this.nodes.filter(x => x !== siblingNode)];
            return;
        }
        this.nodes.push(new Graph([],node.fullName, node));
    }

    private findNodeThatIsPrefix(newFullName: string): Graph | undefined {
        return this.nodes.find(n => newFullName.startsWith(n.fullName + '.'));
    }

    private findSiblingNode(newFullName: string): Graph | undefined {
        return this.nodes.find(n => {
            const sharedPrefix = this.getSharedPrefixUntilDot(n.fullName, newFullName);
            if (!sharedPrefix) {
                return false;
            }
            return sharedPrefix !== this.fullName;

        });
    }

    private createSharedParent(siblingNode: Graph, node: NodeDefinition): Graph {
        const sharedPrefix = this.getSharedPrefixUntilDot(siblingNode.fullName, node.fullName);
        if (!sharedPrefix) {
            throw 'Cannot create shared parent if there is no shared prefix...';
        }
        const newNode = new Graph([],node.fullName, node);
        return new Graph([siblingNode, newNode], sharedPrefix,undefined);
    }

    private getSharedPrefixUntilDot(s1: string, s2: string): string | undefined {
        const split1 = s1.split('.');
        const split2 = s2.split('.');
        for(let i = 0; i< split1.length; i++ ) {
            if(split1[i] !== split2[i]) {
                if (i===0) {
                    return undefined;
                } else {
                    return split1.slice(0, i).join('.');
                }
            }
        }
        return s1;
    }

    public removeClasses(showClasses: ShowClasses): Graph {
        //TODO merge if only one kid remains
        if (showClasses === 'HIDE_INNER') {
            const withoutInner = this.nodes
                .filter(n => !n.nodeDefinition || !n.fullName.includes('$'))
                .map(n => n.removeClasses(showClasses));
            return new Graph(withoutInner, this.fullName, this.nodeDefinition);
        } else if (showClasses === 'HIDE_ALL') {
            const withoutClasses = this.nodes
                .filter(n => !n.nodeDefinition)
                .map(n => n.removeClasses(showClasses));
            return new Graph(withoutClasses, this.fullName, this.nodeDefinition);
        } else if (showClasses === 'SHOW_ALL') {
            return this;
        } else {
            throw 'Invalid show classes value ' + showClasses;
        }
    }

    public removeIgnored(ignoredPackages: string[]): Graph {
        //TODO merge if only one kid remains
        const newNodes = this.nodes
            .filter(n => !ignoredPackages.find(i => n.fullName.startsWith(i)))
            .map(n => n.removeIgnored(ignoredPackages));
        return new Graph(newNodes, this.fullName, this.nodeDefinition);
    }

    public collapseCollapsed(collapsePackages: string[]): Graph {
        const collapsedName = collapsePackages
            .filter(c => this.fullName.startsWith(c))
            .sort((a,b) => a.length - b.length)
            .pop();
        if(collapsedName && collapsedName === this.fullName) {
            return new Graph([],this.fullName, this.nodeDefinition);
        }
        if(collapsedName) {
            return new Graph([],collapsedName, undefined);
        }
        const newNodes = this.nodes
            .map(n => n.collapseCollapsed(collapsePackages));
        return new Graph(newNodes, this.fullName, this.nodeDefinition);
    }

    public takeBase(basePackage: string | undefined): Graph {
        if (!basePackage) {
            return this;
        }
        const nextLevel = this.findNodeThatIsPrefixOrEqual(basePackage);
        if(!nextLevel){
            return this;
        }
        if(this.fullName === ''){
            return new Graph([nextLevel.takeBase(basePackage)],'', undefined);
        }
        return nextLevel.takeBase(basePackage);
    }

    private findNodeThatIsPrefixOrEqual(newFullName: string): Graph | undefined{
        return this.nodes.find(n => newFullName.startsWith(n.fullName));
    }

    public addDependencies(dependencies: Dependency[]): DependencyGraph {
        const map  = new Map<Graph, Dependency[]>();
        dependencies.forEach(d => {
            const commonParent = this.findCommonParent(d.from, d.to);
            const oldDependencies = map.get(commonParent);
            map.set(commonParent, oldDependencies ? [...oldDependencies, d]: [d]);
        });
        return this.toDependencyGraph(map, undefined);
    }

    private findCommonParent(fullName1: string, fullName2: string): Graph {
        const nextLevel = this.nodes.find(n => {
            const name = n.fullName;
            return fullName1.startsWith(name) && fullName2.startsWith(name);
        });
        return nextLevel ? nextLevel.findCommonParent(fullName1, fullName2) : this;
    }

    private getChildInDirection(fullName: string): Graph | undefined {
        return this.nodes.find(n => fullName.startsWith(n.fullName));
    }

    private getName(parent: Graph): string {
        return parent.fullName === '' ? this.fullName :this.fullName.substring(parent.fullName.length+1);
    }

    private toDependencyGraph(dependencies: Map<Graph, Dependency[]>, parent?: Graph): DependencyGraph {
        return {
            name: parent ? this.getName(parent) : this.fullName,
            dependencies: (dependencies.get(this) ?? [])
                .map(d => this.convertToNodeDependency(d))
                .reduce<Dependency[]>((res, d) => (d ? [...res, d] : res), []),
            nodes: this.nodes.map(n => n.toDependencyGraph(dependencies, this)),
            nodeDefinition: this.nodeDefinition
        };
    }

    private convertToNodeDependency(d: Dependency): Dependency | undefined {
        const fromNodeInParent = this.getChildInDirection(d.from);
        if(!fromNodeInParent) {
            return;
        }
        const toNodeInParent = this.getChildInDirection(d.to);
        if(!toNodeInParent) {
            return;
        }
        return {
            from: fromNodeInParent.getName(this),
            to: toNodeInParent.getName(this)
        };
    }
}
