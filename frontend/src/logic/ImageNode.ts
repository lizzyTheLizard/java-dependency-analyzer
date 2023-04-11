import {Attribute} from './Input';

export type Type = 'CLASS' | 'PACKAGE';

export interface ImageNode {
    readonly attributes: Attribute[],
    readonly type: Type,
    readonly fullName: string,
}
