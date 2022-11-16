import jquery from 'jquery';
import {data} from './dummyData';
import './styles.scss';
import {jDepsReader} from './input/JDepsReader';
import {update} from './update';
import {save} from './save';

const jDepsInput = jDepsReader(data);
update(jDepsInput);
jquery('#updateButton').on('click', () => update(jDepsInput));
jquery('#saveButton').on('click', () => save());


