import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { JobTaskOrderDetailComponent } from 'app/entities/job-task-order/job-task-order-detail.component';
import { JobTaskOrder } from 'app/shared/model/job-task-order.model';

describe('Component Tests', () => {
  describe('JobTaskOrder Management Detail Component', () => {
    let comp: JobTaskOrderDetailComponent;
    let fixture: ComponentFixture<JobTaskOrderDetailComponent>;
    const route = ({ data: of({ jobTaskOrder: new JobTaskOrder(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [JobTaskOrderDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(JobTaskOrderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobTaskOrderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jobTaskOrder on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jobTaskOrder).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
