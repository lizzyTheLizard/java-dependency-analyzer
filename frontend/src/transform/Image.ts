import {data} from '../dummyData';
import {jDepsReader} from '../input/JDepsReader';
import {writeSvg} from '../output/NomnomlWriter';
import {Filter, GraphNode} from './Graph';
import {removeClasses, RemoveClassesSelection} from './RemoveClasses';
import {basePackage} from './BasePackage';
import {removePackages} from './RemovePackages';
import {collapsePackages} from './CollapsePackages';

export class Image {
    public constructor(
        private readonly _input = jDepsReader(data),
        public readonly base: GraphNode | undefined = undefined,
        public readonly collapsed: GraphNode[] = [],
        public readonly ignored: GraphNode[] = [],
        public readonly showClasses: RemoveClassesSelection = 'SHOW_ALL'
    ) { }

    public setShowClasses(showClasses: RemoveClassesSelection): Image{
        return new Image(this._input, this.base, this.collapsed, this.ignored, showClasses);
    }

    public setBase(base?: GraphNode): Image{
        return new Image(this._input, base, this.collapsed, this.ignored, this.showClasses);
    }

    public ignore(node: GraphNode): Image{
        const ignored = this.ignored.includes(node)
            ? this.ignored.filter(n => n !== node)
            : [...this.ignored, node];
        return new Image(this._input, this.base, this.collapsed, ignored, this.showClasses);
    }

    public collapse(node: GraphNode): Image{
        const collapsed = this.collapsed.includes(node)
            ? this.collapsed.filter(n => n !== node)
            : [...this.collapsed, node];
        return new Image(this._input, this.base, collapsed, this.ignored, this.showClasses);
    }

    public findNode(fullName: string): GraphNode {
        return this._input.findNode(fullName);
    }

    public getSvgImage(): string{
        const graph = this._input.filter(this.getFilters());
        return writeSvg(graph);
    }

    private getFilters(): Filter[] {
        const filters: Filter[] = [];
        filters.push(removeClasses(this.showClasses));
        filters.push(basePackage(this.base));
        filters.push(...removePackages(this.ignored));
        filters.push(...collapsePackages(this.collapsed));
        return filters;
    }

}
