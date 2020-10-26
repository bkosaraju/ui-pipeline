import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JobExecutionService } from 'app/entities/job-execution/job-execution.service';
import { IJobExecution, JobExecution } from 'app/shared/model/job-execution.model';

describe('Service Tests', () => {
  describe('JobExecution Service', () => {
    let injector: TestBed;
    let service: JobExecutionService;
    let httpMock: HttpTestingController;
    let elemDefault: IJobExecution;
    let expectedResult: IJobExecution | IJobExecution[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(JobExecutionService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new JobExecution(0, currentDate, 'AAAAAAA', currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            jobOrderTimestamp: currentDate.format(DATE_TIME_FORMAT),
            jobExecutionEndTimestamp: currentDate.format(DATE_TIME_FORMAT),
            jobExecutionStartTimestamp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a JobExecution', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            jobOrderTimestamp: currentDate.format(DATE_TIME_FORMAT),
            jobExecutionEndTimestamp: currentDate.format(DATE_TIME_FORMAT),
            jobExecutionStartTimestamp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            jobOrderTimestamp: currentDate,
            jobExecutionEndTimestamp: currentDate,
            jobExecutionStartTimestamp: currentDate,
          },
          returnedFromService
        );

        service.create(new JobExecution()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a JobExecution', () => {
        const returnedFromService = Object.assign(
          {
            jobOrderTimestamp: currentDate.format(DATE_TIME_FORMAT),
            jobExecutionStatus: 'BBBBBB',
            jobExecutionEndTimestamp: currentDate.format(DATE_TIME_FORMAT),
            jobExecutionStartTimestamp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            jobOrderTimestamp: currentDate,
            jobExecutionEndTimestamp: currentDate,
            jobExecutionStartTimestamp: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of JobExecution', () => {
        const returnedFromService = Object.assign(
          {
            jobOrderTimestamp: currentDate.format(DATE_TIME_FORMAT),
            jobExecutionStatus: 'BBBBBB',
            jobExecutionEndTimestamp: currentDate.format(DATE_TIME_FORMAT),
            jobExecutionStartTimestamp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            jobOrderTimestamp: currentDate,
            jobExecutionEndTimestamp: currentDate,
            jobExecutionStartTimestamp: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a JobExecution', () => {
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
