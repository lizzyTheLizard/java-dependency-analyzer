import {simpleTestGraph} from '../transform/TestData';
import {Image} from './Image';
import {writeSvg} from './writeSvg';

test('Construct', () => {
    const target = new Image(simpleTestGraph);
    expect(target.base).toBeUndefined();
    expect(target.collapsed).toEqual([]);
    expect(target.ignored).toEqual([]);
    expect(target.showClasses).toEqual('SHOW_ALL');
});

test('getSvgImage', () => {
    const target = new Image(simpleTestGraph);
    const svg = target.getSvgImage();
    expect(svg).toEqual(writeSvg(simpleTestGraph));
});

test('findNode', () => {
    const target = new Image(simpleTestGraph);
    const node = target.findNode('package1.test1');
    expect(node).toEqual({
        nodes: [],
        dependencies: [],
        attributes: [],
        name: 'test1',
        fullName: 'package1.test1',
        type: 'CLASS'
    });
});

test('setBase', () => {
    const input = new Image(simpleTestGraph);
    const target = input.setBase(input.findNode('package1.test1'));
    expect(target.base).toEqual({
        nodes: [],
        dependencies: [],
        attributes: [],
        name: 'test1',
        fullName: 'package1.test1',
        type: 'CLASS'
    });
    expect(target.collapsed).toEqual([]);
    expect(target.ignored).toEqual([]);
    expect(target.showClasses).toEqual('SHOW_ALL');
});

test('collapse', () => {
    const input = new Image(simpleTestGraph);
    const target = input.collapse(input.findNode('package1.test1'));
    expect(target.base).toBeUndefined();
    expect(target.collapsed).toEqual([{
        nodes: [],
        dependencies: [],
        attributes: [],
        name: 'test1',
        fullName: 'package1.test1',
        type: 'CLASS'
    }]);
    expect(target.ignored).toEqual([]);
    expect(target.showClasses).toEqual('SHOW_ALL');
});

test('collapse', () => {
    const input = new Image(simpleTestGraph);
    const target = input.ignore(input.findNode('package1.test1'));
    expect(target.base).toBeUndefined();
    expect(target.collapsed).toEqual([]);
    expect(target.ignored).toEqual([{
        nodes: [],
        dependencies: [],
        attributes: [],
        name: 'test1',
        fullName: 'package1.test1',
        type: 'CLASS'
    }]);
    expect(target.showClasses).toEqual('SHOW_ALL');
});

test('setShowClasses', () => {
    const input = new Image(simpleTestGraph);
    const target = input.setShowClasses('HIDE_ALL');
    expect(target.base).toBeUndefined();
    expect(target.collapsed).toEqual([]);
    expect(target.ignored).toEqual([]);
    expect(target.showClasses).toEqual('HIDE_ALL');
});
