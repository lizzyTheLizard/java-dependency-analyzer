import {Attribute, Dependency} from '../image/InputFile';

export interface Graph {
    readonly nodes: GraphNode[];
    readonly dependencies: GraphDependency[];
    readonly attributes: Attribute[];

    filter(filters: Filter[]): Graph;

    findNode(fullName: string, createMissing?: (name: string, fullName: string, type: Type) => GraphNode): GraphNode;
}

export interface GraphNode extends Graph {
    readonly name: string;
    readonly fullName: string;
    readonly type: Type;

    getShortenFullName(length: number): string;

    isChildOf(packages: GraphNode[]): boolean;
}


export type Type = 'CLASS' | 'PACKAGE';

export type GraphDependency = {
    from: string;
    to: string;
    dependencies: Dependency[];
};

export type Filter = (input: Graph) => Graph;
