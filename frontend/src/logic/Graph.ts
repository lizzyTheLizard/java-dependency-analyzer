import {NodeDefinition, ShowClasses} from './Input';

export class Graph {
    constructor(
        public readonly fullName: string,
        private _nodes: Graph[],
        public readonly nodeDefinition: NodeDefinition | undefined
    ) {  }

    public get nodes(): Graph[] {
        return this._nodes;
    }

    public getName(parent: Graph): string {
        return parent.fullName === '' ? this.fullName :this.fullName.substring(parent.fullName.length+1);
    }

    public addNode(node: NodeDefinition) {
        const prefixNode = this.findNodeThatIsPrefix(node.fullName);
        if (prefixNode) {
            prefixNode.addNode(node);
            return;
        }
        const siblingNode = this.findSiblingNode(node.fullName);
        if (siblingNode) {
            const newParent = this.createSharedParent(siblingNode, node);
            this._nodes = [newParent, ...this._nodes.filter(x => x !== siblingNode)];
            return;
        }
        this._nodes.push(new Graph(node.fullName, [], node));
    }

    private findNodeThatIsPrefix(newFullName: string): Graph | undefined {
        return this._nodes.find(n => newFullName.startsWith(n.fullName));
    }

    private findSiblingNode(newFullName: string): Graph | undefined {
        return this._nodes.find(n => {
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
        const newNode = new Graph(node.fullName, [], node);
        return new Graph(sharedPrefix, [siblingNode, newNode], undefined);
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
        if (showClasses === 'HIDE_INNER') {
            const withoutInner = this._nodes
                .filter(n => !n.nodeDefinition || !n.fullName.includes('$'))
                .map(n => n.removeClasses(showClasses));
            return new Graph(this.fullName, withoutInner, this.nodeDefinition);
        } else if (showClasses === 'HIDE_ALL') {
            const withoutClasses = this._nodes
                .filter(n => !n.nodeDefinition)
                .map(n => n.removeClasses(showClasses));
            return new Graph(this.fullName, withoutClasses, this.nodeDefinition);
        } else if (showClasses === 'SHOW_ALL') {
            return this;
        } else {
            throw 'Invalid show classes value ' + showClasses;
        }
    }

    public removeIgnored(ignoredPackages: string[]): Graph {
        const newNodes = this._nodes
            .filter(n => !ignoredPackages.find(i => n.fullName.startsWith(i)))
            .map(n => n.removeIgnored(ignoredPackages));
        return new Graph(this.fullName, newNodes, this.nodeDefinition);
    }

    public collapseCollapsed(collapsePackages: string[]): Graph {
        const collapsedName = collapsePackages
            .filter(c => this.fullName.startsWith(c))
            .sort((a,b) => a.length - b.length)
            .pop();
        if(collapsedName && collapsedName === this.fullName) {
            return new Graph(this.fullName, [], this.nodeDefinition);
        }
        if(collapsedName) {
            return new Graph(collapsedName, [], undefined);
        }
        const newNodes = this._nodes
            .map(n => n.collapseCollapsed(collapsePackages));
        return new Graph(this.fullName, newNodes, this.nodeDefinition);
    }

    public takeBase(basePackage: string | undefined): Graph {
        if (!basePackage) {
            return this;
        }
        const nextLevel = this.findNodeThatIsPrefix(basePackage);
        if(!nextLevel){
            return this;
        }
        return nextLevel.takeBase(basePackage);
    }

}






export function initializeGraph(nodes: NodeDefinition[]){
    const graph = new Graph('', [] , undefined);
    nodes.forEach(n => graph.addNode(n));
    return graph;
}
