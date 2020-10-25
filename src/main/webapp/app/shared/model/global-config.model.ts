export interface IGlobalConfig {
  id?: number;
  configKey?: string;
  configValue?: string;
  configType?: string;
}

export class GlobalConfig implements IGlobalConfig {
  constructor(public id?: number, public configKey?: string, public configValue?: string, public configType?: string) {}
}
