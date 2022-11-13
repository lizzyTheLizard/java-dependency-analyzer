export type Graph = {
	readonly nodes: GraphNode[];
	readonly dependencies: GraphDependency[];
};

export type GraphNode = {
	readonly name: string;
	readonly type: Type;
} & Graph;

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
