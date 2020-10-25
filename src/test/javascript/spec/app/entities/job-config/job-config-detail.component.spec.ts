import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { JobConfigDetailComponent } from 'app/entities/job-config/job-config-detail.component';
import { JobConfig } from 'app/shared/model/job-config.model';

describe('Component Tests', () => {
  describe('JobConfig Management Detail Component', () => {
    let comp: JobConfigDetailComponent;
    let fixture: ComponentFixture<JobConfigDetailComponent>;
    const route = ({ data: of({ jobConfig: new JobConfig(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [JobConfigDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(JobConfigDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobConfigDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jobConfig on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jobConfig).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
