import { GraphImpl, GraphNodeImpl } from './GraphImpl';

const test1 = new GraphNodeImpl([], [], 'test1', 'package1.test1', 'CLASS');
const test2 = new GraphNodeImpl([], [], 'test2', 'package1.test2', 'CLASS');
const dependency1 = { from: test1.name, to: test2.name, dependencies: [{from: test1.fullName, to: test2.fullName}]};
const package1 = new GraphNodeImpl([test1, test2], [dependency1], 'package1', 'package1', 'PACKAGE');
const package2 = new GraphNodeImpl([], [], 'package2', 'package2', 'PACKAGE');
const dependency2 = { from: package1.name, to: package2.name, dependencies: [{from: package1.fullName, to: package2.fullName}]};
export const simpleTestGraph = new GraphImpl([package1, package2], [dependency2]);
