import * as nomnoml from 'nomnoml';
import {DependencyGraph} from './Graph';
import {Dependency} from './Input';

export function writeSvg(graph: DependencyGraph): string {
    const nomnomlText = graph.nodes
        .map(n => nodeToNomnoml(n, ''))
        .join('');
    return nomnoml.renderSvg(nomnomlText);
}

function nodeToNomnoml(input: DependencyGraph, intend: string): string {
    if (input.nodeDefinition) {
        //This is a class => draw it normally
        return `${intend}[${input.name}]\n`;
    }
    const innerIntend = intend + '  ';
    const children = input.nodes.map(n => nodeToNomnoml(n, innerIntend)).join('');
    const dependencies = input.dependencies.map(d => dependencyToNomnoml(d, innerIntend)).join('');
    const content = children + dependencies;
    if (content.length === 0) {
        //This is a package without content => draw it simply
        return `${intend}[<package> ${input.name}]\n`;
    }
    //This is a package with content => draw it with content
    return `${intend}[<package> ${input.name}|\n${content}${intend}]\n`;
}

function dependencyToNomnoml(d: Dependency, intend: string): string {
    return `${intend}[${d.from}] --> [${d.to}]\n`;
}
