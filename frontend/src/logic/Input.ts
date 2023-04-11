export interface Input {
    readonly properties: Properties,
    readonly nodes: NodeDefinition[],
    readonly dependencies: Dependency[],
}

export interface Properties {
    readonly name: string,
    readonly version: string,
    readonly basePackage?: string,
    readonly collapsePackages: string[],
    readonly ignoredPackages: string[],
    readonly splitPackages: string[],
    readonly showClasses: ShowClasses
}

export type ShowClasses = 'HIDE_INNER' | 'HIDE_ALL' | 'SHOW_ALL';

export type Dependency = {
    readonly from: string;
    readonly to: string;
};

export interface NodeDefinition {
    readonly fullName: string,
    readonly attributes: Attribute[]
}

export interface Attribute {
    readonly type: AttributeType;
    readonly value: string;
    readonly name: string;
}

export type AttributeType = 'LINK' | 'TEXT' | 'BOOLEAN' | 'NUMBER';
