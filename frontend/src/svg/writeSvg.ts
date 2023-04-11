import {Dependency, NodeDefinition, Properties} from '../image/Input';

export function writeSvg(nodes: NodeDefinition[], dependencies: Dependency[], properties: Properties): string {
    //TODO Remove classes if not shown
    //TODO Remove ignored nodes
    //TODO Remove collapsed nodes
    //TODO Distribute Dependencies
    //TODO Get rid of all but base image
    //TODO Transform to nomnoml
    return 'dummy' + nodes.length + dependencies.length + (properties.basePackage ?? '');
}
