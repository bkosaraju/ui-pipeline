import { ConfigType } from 'app/shared/model/enumerations/config-type.model';

export interface ITaskConfig {
  id?: number;
  configKey?: string;
  configValue?: string;
  configType?: ConfigType;
  configVersion?: number;
}

export class TaskConfig implements ITaskConfig {
  constructor(
    public id?: number,
    public configKey?: string,
    public configValue?: string,
    public configType?: ConfigType,
    public configVersion?: number
  ) {}
}
