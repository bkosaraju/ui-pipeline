import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TaskExecutionConfigService } from 'app/entities/task-execution-config/task-execution-config.service';
import { ITaskExecutionConfig, TaskExecutionConfig } from 'app/shared/model/task-execution-config.model';

describe('Service Tests', () => {
  describe('TaskExecutionConfig Service', () => {
    let injector: TestBed;
    let service: TaskExecutionConfigService;
    let httpMock: HttpTestingController;
    let elemDefault: ITaskExecutionConfig;
    let expectedResult: ITaskExecutionConfig | ITaskExecutionConfig[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TaskExecutionConfigService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new TaskExecutionConfig(0, 'AAAAAAA', 'AAAAAAA', 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TaskExecutionConfig', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TaskExecutionConfig()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TaskExecutionConfig', () => {
        const returnedFromService = Object.assign(
          {
            configKey: 'BBBBBB',
            configValue: 'BBBBBB',
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

      it('should return a list of TaskExecutionConfig', () => {
        const returnedFromService = Object.assign(
          {
            configKey: 'BBBBBB',
            configValue: 'BBBBBB',
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

      it('should delete a TaskExecutionConfig', () => {
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
