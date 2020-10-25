import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { JobExecutionDetailComponent } from 'app/entities/job-execution/job-execution-detail.component';
import { JobExecution } from 'app/shared/model/job-execution.model';

describe('Component Tests', () => {
  describe('JobExecution Management Detail Component', () => {
    let comp: JobExecutionDetailComponent;
    let fixture: ComponentFixture<JobExecutionDetailComponent>;
    const route = ({ data: of({ jobExecution: new JobExecution(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [JobExecutionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(JobExecutionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobExecutionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jobExecution on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jobExecution).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
