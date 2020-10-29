import { Moment } from 'moment';
import { TaskType } from 'app/shared/model/enumerations/task-type.model';

export interface ITask {
  id?: number;
  taskName?: string;
  taskType?: TaskType;
  createTimestamp?: Moment;
}

export class Task implements ITask {
  constructor(public id?: number, public taskName?: string, public taskType?: TaskType, public createTimestamp?: Moment) {}
}
