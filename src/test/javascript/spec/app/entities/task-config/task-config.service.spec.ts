import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TaskConfigService } from 'app/entities/task-config/task-config.service';
import { ITaskConfig, TaskConfig } from 'app/shared/model/task-config.model';

describe('Service Tests', () => {
  describe('TaskConfig Service', () => {
    let injector: TestBed;
    let service: TaskConfigService;
    let httpMock: HttpTestingController;
    let elemDefault: ITaskConfig;
    let expectedResult: ITaskConfig | ITaskConfig[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TaskConfigService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new TaskConfig(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TaskConfig', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TaskConfig()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TaskConfig', () => {
        const returnedFromService = Object.assign(
          {
            configKey: 'BBBBBB',
            configValue: 'BBBBBB',
            configType: 'BBBBBB',
            configVersion: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TaskConfig', () => {
        const returnedFromService = Object.assign(
          {
            configKey: 'BBBBBB',
            configValue: 'BBBBBB',
            configType: 'BBBBBB',
            configVersion: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TaskConfig', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
