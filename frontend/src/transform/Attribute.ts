export type AttributeType = 'LINK' | 'TEXT';

export interface Attribute {
    type: AttributeType;
    value: string;
    name: string;
}
