export type AttributeType = 'LINK' | 'TEXT' | 'BOOLEAN' | 'NUMBER';

export interface Attribute {
    type: AttributeType;
    value: string;
    name: string;
}
