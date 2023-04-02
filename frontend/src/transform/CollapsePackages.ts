import type {Filter, GraphNode} from './Graph';
import {GraphImpl, GraphNodeImpl} from './GraphImpl';

export function collapsePackages(packages: GraphNode[]): Filter[] {
    return packages.map(toCollapse => (graph => new GraphImpl(
        graph.nodes.map(n => collapsePackagesRecursive(n, toCollapse)),
        graph.dependencies
    )));
}

function collapsePackagesRecursive(graph: GraphNode, toCollapse: GraphNode): GraphNode {
    const collapseNode = graph === toCollapse;
    const newChildNodes = collapseNode ? [] : graph.nodes.map(n => collapsePackagesRecursive(n, toCollapse));
    const newDependencies = collapseNode ? [] : graph.dependencies;
    return new GraphNodeImpl(
        newChildNodes,
        newDependencies,
        graph.name,
        graph.fullName,
        graph.type
    );
}

