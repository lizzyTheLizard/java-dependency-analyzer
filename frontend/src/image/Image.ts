import {writeSvg} from './writeSvg';
import {Filter, Graph, GraphNode} from '../transform/Graph';
import {removeClasses, RemoveClassesSelection} from '../transform/RemoveClasses';
import {basePackage} from '../transform/BasePackage';
import {removePackages} from '../transform/RemovePackages';
import {collapsePackages} from '../transform/CollapsePackages';
import {GraphImpl} from '../transform/GraphImpl';

export class Image {
    public constructor(
        private readonly _graph: Graph = new GraphImpl([],[],[]),
        public readonly base: GraphNode | undefined = undefined,
        public readonly collapsed: GraphNode[] = [],
        public readonly ignored: GraphNode[] = [],
        public readonly showClasses: RemoveClassesSelection = 'SHOW_ALL'
    ) {
    }

    public setShowClasses(showClasses: RemoveClassesSelection): Image{
        return new Image(this._graph, this.base, this.collapsed, this.ignored, showClasses);
    }

    public setBase(base?: GraphNode): Image{
        return new Image(this._graph, base, this.collapsed, this.ignored, this.showClasses);
    }

    public ignore(node: GraphNode): Image{
        const ignored = this.ignored.includes(node)
            ? this.ignored.filter(n => n !== node)
            : [...this.ignored, node];
        return new Image(this._graph, this.base, this.collapsed, ignored, this.showClasses);
    }

    public collapse(node: GraphNode): Image{
        const collapsed = this.collapsed.includes(node)
            ? this.collapsed.filter(n => n !== node)
            : [...this.collapsed, node];
        return new Image(this._graph, this.base, collapsed, this.ignored, this.showClasses);
    }

    public findNode(fullName: string): GraphNode {
        return this._graph.findNode(fullName);
    }

    public getSvgImage(): string{
        const graph = this._graph.filter(this.getFilters());
        return writeSvg(graph);
    }

    private getFilters(): Filter[] {
        const filters: Filter[] = [];
        filters.push(basePackage(this.base));
        filters.push(...collapsePackages(this.collapsed));
        filters.push(...removePackages(this.ignored));
        filters.push(removeClasses(this.showClasses));
        return filters;
    }
}
