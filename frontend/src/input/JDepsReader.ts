import type {Dependency, Graph} from '../transform/Graph';
import {GraphImpl} from './GraphImpl';

export function jDepsReader(jdeps: string): Graph {
	const dependencies: Dependency[] = [];
	jdeps.split('\n').forEach(l => {
		const lineResult = lineReader(l);
		if (lineResult) {
			dependencies.push(lineResult);
		}
	});

	const initial = new GraphImpl();
	dependencies.forEach(d => {
		initial.addNode(d.from);
		initial.addNode(d.to);
	});
	dependencies.forEach(d => {
		initial.addDependency(d);
	});
	return initial;
}

function lineReader(line: string): Dependency | undefined {
	if (line.length < 5) {
		return undefined;
	}

	const parts = line.split('->');
	if (parts.length <= 1) {
		return undefined;
	}

	const from = parts[0].trim();
	parts[1] = parts[1].trim();
	const lastSpace = parts[1].indexOf(' ');
	const to = parts[1].substring(0, lastSpace).trim();
	if (to.length === 0 || to === 'not') {
		return undefined;
	}

	return {
		from,
		to,
	};
}
