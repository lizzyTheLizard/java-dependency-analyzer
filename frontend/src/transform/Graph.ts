export interface Graph {
	readonly nodes: GraphNode[];
	readonly dependencies: GraphDependency[];
	filter(filters: Filter[]): Graph;
	findNode(fullName: string, createMissing?: (name: string, fullName: string, type: Type) => GraphNode): GraphNode;
}

export interface GraphNode extends Graph {
	readonly name: string;
	readonly fullName: string;
	readonly type: Type;
}

export type Type = 'CLASS' | 'PACKAGE';

export type GraphDependency = {
	from: string;
	to: string;
	dependencies: Dependency[];
};

export type Dependency = {
	readonly from: string;
	readonly to: string;
};

export type Filter = (input: Graph) => Graph;
