import type {Dependency, Graph, GraphDependency, GraphNode} from '../transform/Graph';

export class GraphImpl implements Graph {
	public readonly nodes: GraphNode[] = [];
	public readonly dependencies: GraphDependency[] = [];

	public addNode(name: string): void {
		this.findNode(name.split('.'), (n, last) => ({
			name: n,
			type: last ? 'CLASS' : 'PACKAGE',
			nodes: [],
			dependencies: [],
		}));
	}

	public addDependency(d: Dependency): void {
		const commonStart: string[] = [];
		let fromNext = '';
		let toNext = '';

		const fromSplit = d.from.split('.');
		const toSplit = d.to.split('.');
		for (let i = 0; i < Math.min(fromSplit.length, toSplit.length); i++) {
			if (fromSplit[i] === toSplit[i]) {
				commonStart.push(fromSplit[i]);
			} else {
				fromNext = fromSplit[i];
				toNext = toSplit[i];
				break;
			}
		}

		const baseNode = this.findNode(commonStart, () => {
			throw new Error('Node ' + commonStart.join('.') + ' not found');
		});

		const existingDependency = baseNode.dependencies.find(n => n.from === fromNext && n.to === toNext);
		if (existingDependency) {
			existingDependency.dependencies.push(d);
		} else {
			const newDependency = {from: fromNext, to: toNext, dependencies: [d]};
			baseNode.dependencies.push(newDependency);
		}
	}

	private findNode(parts: string[], createMissing: (name: string, isLast: boolean) => GraphNode): Graph {
		// eslint-disable-next-line @typescript-eslint/no-this-alias
		let currentNode: Graph = this;
		parts.forEach((p, i) => {
			const existingNode = currentNode.nodes.find(n => n.name === p);
			if (existingNode) {
				currentNode = existingNode;
			} else {
				const newNode = createMissing(p, parts.length - 1 === i);
				currentNode.nodes.push(newNode);
				currentNode = newNode;
			}
		});
		return currentNode;
	}
}
