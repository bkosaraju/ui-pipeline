import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { TaskExecutionConfigDetailComponent } from 'app/entities/task-execution-config/task-execution-config-detail.component';
import { TaskExecutionConfig } from 'app/shared/model/task-execution-config.model';

describe('Component Tests', () => {
  describe('TaskExecutionConfig Management Detail Component', () => {
    let comp: TaskExecutionConfigDetailComponent;
    let fixture: ComponentFixture<TaskExecutionConfigDetailComponent>;
    const route = ({ data: of({ taskExecutionConfig: new TaskExecutionConfig(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [TaskExecutionConfigDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TaskExecutionConfigDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaskExecutionConfigDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load taskExecutionConfig on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.taskExecutionConfig).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
