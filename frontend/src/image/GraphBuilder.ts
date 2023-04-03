import type {Dependency} from '../transform/Graph';
import {GraphImpl, GraphNodeImpl} from '../transform/GraphImpl';
import {Attribute} from '../transform/Attribute';

interface NodeDefinition {
    fullName: string,
    attributes: Attribute[]
}
interface Input {
    nodes: NodeDefinition[],
    dependencies: Dependency[],
}

export class GraphBuilder extends GraphImpl {
    constructor(inputString: string) {
        super([], [], []);
        const input: Input = JSON.parse(inputString);
        input.nodes.forEach(n =>this.addNode(n));
        input.dependencies.forEach(d => this.addDependency(d));
    }

    private addNode(node: NodeDefinition): void {
        this.findNode(node.fullName, (name, fullName, type) => new GraphNodeImpl([], [], node.attributes, name, fullName, type));
    }

    private addDependency(d: Dependency): void {
        const common = this.findCommon(d);
        const baseNode = common.root ? this.findNode(common.root) : this;
        const existingDependency = baseNode.dependencies.find(n => n.from === common.fromNext && n.to === common.toNext);
        if (existingDependency) {
            existingDependency.dependencies.push(d);
        } else {
            const newDependency = {from: common.fromNext, to: common.toNext, dependencies: [d]};
            baseNode.dependencies.push(newDependency);
        }
    }

    private findCommon(d: Dependency): { root: string, fromNext: string, toNext: string } {
        const commonStart: string[] = [];
        const fromSplit = d.from.split('.');
        const toSplit = d.to.split('.');
        for (let i = 0; i < Math.min(fromSplit.length, toSplit.length); i++) {
            if (fromSplit[i] === toSplit[i]) {
                commonStart.push(fromSplit[i]);
            } else {
                return {
                    root: commonStart.join('.'),
                    fromNext: fromSplit[i],
                    toNext: toSplit[i],
                };
            }
        }
        throw new Error('Identical');
    }
}
