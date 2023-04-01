import {jDepsReader} from './JDepsReader';

test('Simple', () => {
    const target = jDepsReader('package.test1 -> package.test2 not found');
    expect(target).toEqual({
        dependencies: [],
        nodes: [
            {
                name: 'package',
                fullName: 'package',
                type: 'PACKAGE',
                dependencies: [
                    {
                        from: 'test1',
                        to: 'test2',
                        dependencies: [
                            {from: 'package.test1', to: 'package.test2'},
                        ],
                    },
                ],
                nodes: [
                    {
                        name: 'test1',
                        fullName: 'package.test1',
                        type: 'CLASS',
                        dependencies: [],
                        nodes: [],
                    }, {
                        name: 'test2',
                        fullName: 'package.test2',
                        type: 'CLASS',
                        dependencies: [],
                        nodes: [],
                    },
                ],
            },
        ],
    });
});

test('Multiline', () => {
    const target = jDepsReader('package.test1 -> package.test2 not found\n package.test1 -> package.test3 not found');
    expect(target).toEqual({
        dependencies: [],
        nodes: [
            {
                name: 'package',
                fullName: 'package',
                type: 'PACKAGE',
                dependencies: [
                    {
                        from: 'test1',
                        to: 'test2',
                        dependencies: [
                            {from: 'package.test1', to: 'package.test2'},
                        ],
                    }, {
                        from: 'test1',
                        to: 'test3',
                        dependencies: [
                            {from: 'package.test1', to: 'package.test3'},
                        ],
                    },
                ],
                nodes: [
                    {
                        name: 'test1',
                        fullName: 'package.test1',
                        type: 'CLASS',
                        dependencies: [],
                        nodes: [],
                    }, {
                        name: 'test2',
                        fullName: 'package.test2',
                        type: 'CLASS',
                        dependencies: [],
                        nodes: [],
                    }, {
                        name: 'test3',
                        fullName: 'package.test3',
                        type: 'CLASS',
                        dependencies: [],
                        nodes: [],
                    },
                ],
            },
        ],
    });
});

test('Invalid Line', () => {
    const target = jDepsReader('Garbage\n package.test1 -> package.test2 not found\n more Garbage');
    expect(target).toEqual({
        dependencies: [],
        nodes: [
            {
                name: 'package',
                fullName: 'package',
                type: 'PACKAGE',
                dependencies: [
                    {
                        from: 'test1',
                        to: 'test2',
                        dependencies: [
                            {from: 'package.test1', to: 'package.test2'},
                        ],
                    },
                ],
                nodes: [
                    {
                        name: 'test1',
                        fullName: 'package.test1',
                        type: 'CLASS',
                        dependencies: [],
                        nodes: [],
                    }, {
                        name: 'test2',
                        fullName: 'package.test2',
                        type: 'CLASS',
                        dependencies: [],
                        nodes: [],
                    },
                ],
            },
        ],
    });
});
