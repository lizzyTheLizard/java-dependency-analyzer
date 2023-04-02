import type {Filter, GraphNode} from './Graph';
import {GraphImpl} from './GraphImpl';

export function basePackage(basePackage?: GraphNode): Filter {
    if(!basePackage) {
        return graph => graph;
    }
    return () => {
        return new GraphImpl([{
            ...basePackage,
            name: basePackage.name,
            fullName: basePackage.fullName,
            type: 'PACKAGE',
        }], []);
    };
}
