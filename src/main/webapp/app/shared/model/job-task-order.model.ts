import { IJob } from 'app/shared/model/job.model';
import { ITask } from 'app/shared/model/task.model';

export interface IJobTaskOrder {
  id?: number;
  taskSeqId?: number;
  jobTaskStatusFlag?: boolean;
  configVersion?: number;
  job?: IJob;
  task?: ITask;
}

export class JobTaskOrder implements IJobTaskOrder {
  constructor(
    public id?: number,
    public taskSeqId?: number,
    public jobTaskStatusFlag?: boolean,
    public configVersion?: number,
    public job?: IJob,
    public task?: ITask
  ) {
    this.jobTaskStatusFlag = this.jobTaskStatusFlag || false;
  }
}
