import {Dependency} from '../transform/Graph';
import {GraphImpl, GraphNodeImpl} from '../transform/GraphImpl';

export class GraphBuilder extends GraphImpl {
	constructor() {
		super([],[]);
	}

	public addNode(name: string): void {
		this.findNode(name, (name, fullname, type) => new GraphNodeImpl([],[], name, fullname, type));
	}

	public addDependency(d: Dependency): void {
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

	private findCommon(d: Dependency): {root: string, fromNext: string, toNext: string} {
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
