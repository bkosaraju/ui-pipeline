import { ConfigType } from 'app/shared/model/enumerations/config-type.model';

export interface IGlobalConfig {
  id?: number;
  configKey?: string;
  configValue?: string;
  configType?: ConfigType;
}

export class GlobalConfig implements IGlobalConfig {
  constructor(public id?: number, public configKey?: string, public configValue?: string, public configType?: ConfigType) {}
}
