import type {Graph, GraphNode} from '../transform/Graph';
import * as nomnoml from 'nomnoml';

export function writeSvg(input?: Graph): string {
    if(!input) {
        return '';
    }
    const nomnomlText = writeNomnoml(input);
    return nomnoml.renderSvg(nomnomlText);
}

export function writeNomnoml(input: GraphNode | Graph): string {
    return input.nodes
        .map(n => writeNode(n))
        .join('\n') + writeDependencies(input);
}

function writeNode(input: GraphNode): string {
    switch (input.type) {
    case 'PACKAGE':
        return `[<package> ${input.name} | ${writeNomnoml(input)}]`;
    default:
        return `[${input.name}]`;
    }
}


function writeDependencies(input: GraphNode | Graph): string {
    if (input.dependencies.length === 0) {
        return '';
    }

    return '\n' + input.dependencies
        .map(d => `[${d.from}] --> [${d.to}]`)
        .join('\n');
}
