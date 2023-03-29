import type {Graph, GraphDependency, GraphNode, Type} from './Graph';
import {Filter} from './Graph';

export class GraphImpl implements Graph {
	constructor(
		public readonly nodes: GraphNode[],
		public readonly dependencies: GraphDependency[],
	) {}

	public filter(filters: Filter[]): Graph {
		if(filters.length === 0) {
			return this;
		}
		const firstFilter = filters[0];
		const remainingFilters = filters.slice(1, filters.length);
		return firstFilter(this).filter(remainingFilters);
	}

	public findNode(fullName: string, createMissing?: (name: string, fullName: string, type: Type) => GraphNode): GraphNode{
		for (const kid of this.nodes) {
			if (fullName.startsWith(kid.name + '.')) {
				const remainingName = fullName.substring(kid.name.length + 1);
				return kid.findNode(remainingName, createMissing);
			}
			if (fullName === kid.name) {
				return kid;
			}
		}
		if (!createMissing) {
			throw new Error('Node ' + fullName + ' not found');
		}
		const newName = fullName.split('.')[0];
		const oldFullName = (this as unknown as GraphNode)?.fullName;
		const newFullName = oldFullName ? oldFullName + '.' + newName : newName;
		if(newName === fullName) {
			const newNode = createMissing(newName, newFullName, 'CLASS');
			this.nodes.push(newNode);
			return newNode;
		} else {
			const newNode = createMissing(newName, newFullName, 'PACKAGE');
			this.nodes.push(newNode);
			return newNode.findNode(fullName.substring(newName.length + 1), createMissing);
		}
	}
}

export class GraphNodeImpl extends GraphImpl implements GraphNode{
	constructor(
		nodes: GraphNode[],
		dependencies: GraphDependency[],
		public readonly name: string,
		public readonly fullName: string,
		public readonly type: Type,
	) { super(nodes, dependencies);}
}