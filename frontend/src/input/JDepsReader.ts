import type {Dependency, Graph} from '../transform/Graph';
import {GraphBuilder} from './GraphBuilder';

export function jDepsReader(jdeps: string): Graph {
	const dependencies: Dependency[] = [];
	jdeps.split('\n').forEach(l => {
		const lineResult = lineReader(l);
		if (lineResult) {
			dependencies.push(lineResult);
		}
	});

	const initial = new GraphBuilder();
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
	const to = parts[1].trim();
	if (to.length === 0 || to === 'not') {
		return undefined;
	}

	return {
		from,
		to,
	};
}
